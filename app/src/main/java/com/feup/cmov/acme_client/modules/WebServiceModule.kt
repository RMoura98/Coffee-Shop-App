package com.feup.cmov.acme_client.modules

import android.util.Base64
import android.util.Log
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.Utils.PreferencesUtils
import com.feup.cmov.acme_client.Utils.Security
import com.feup.cmov.acme_client.network.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object WebServiceModule {

    @Provides
    @Singleton
    fun provideWebService(): WebService {

        // Intercept Request and add Authorization header.
        val builder = OkHttpClient().newBuilder()
        builder
            .addInterceptor(AuthenticatedRequestInterceptor())
        val client = builder.build()

        return Retrofit.Builder()
            .baseUrl(AcmeApplication.getAppContext().getString(R.string.serverURL) + "/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WebService::class.java)
    }


    class AuthenticatedRequestInterceptor : Interceptor {

        fun getDateHeader(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US
            )
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.format(calendar.time)
        }

        override fun intercept(chain: Interceptor.Chain): Response {

            val request: Request = chain.request()
            val invocation: Invocation? = request.tag(Invocation::class.java)
            val authenticatedRequest = invocation?.method()?.getAnnotation(WebService.Companion.AuthenticatedRequest::class.java)
            if (authenticatedRequest != null){
                val (userName, uuid) = PreferencesUtils.getLoggedInUser()
                if (userName == null || uuid == null) return chain.proceed(request)
                val key = Security.retrieveRsaPrivateKey(userName)

                val dateHeader = getDateHeader()
                val url: String
                if (request.url().url().query == null)
                    url = request.url().url().path
                else
                    url = request.url().url().path + "?" + request.url().url().query

                val buffer = okio.Buffer()
                request.body()?.writeTo(buffer)
                buffer.write(",".toByteArray())
                buffer.write(url.toByteArray())
                buffer.write(",".toByteArray())
                buffer.write(dateHeader.toByteArray())

                val signature = Security.makeSignature(key, buffer)
                val authHeader = "$uuid,$signature"
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", authHeader)
                    .addHeader("Date", dateHeader)
                    .build()
                return chain.proceed(newRequest)
            }
            return chain.proceed(request)
        }
    }
}
