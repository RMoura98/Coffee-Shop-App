package com.feup.cmov.acme_client.modules

import android.util.Base64
import android.util.Base64.encodeToString
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
import okhttp3.*
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        override fun intercept(chain: Interceptor.Chain): Response {

            val request: Request = chain.request()
            val invocation: Invocation? = request.tag(Invocation::class.java)
            val authenticatedRequest = invocation?.method()?.getAnnotation(WebService.Companion.AuthenticatedRequest::class.java)
            if (authenticatedRequest != null){
                val (userName, uuid) = PreferencesUtils.getLoggedInUser()
                if (userName == null || uuid == null) return chain.proceed(request)
                val key = Security.retrieveRsaPrivateKey(userName)

                val signature: String
                if(request.method() == "GET" || request.method() == "DELETE") {
                    val toSign: String
                    if (request.url().url().query == null)
                        toSign = request.url().url().path
                    else
                        toSign = request.url().url().path + "?" + request.url().url().query
                    val buffer = okio.Buffer()
                    buffer.write(toSign.toByteArray())
                    signature = Security.makeSignature(key, buffer)
                }
                else { // POST, etc
                    val requestBody = request.body()?: return chain.proceed(request)
                    val buffer = okio.Buffer()
                    requestBody.writeTo(buffer)
                    signature = Security.makeSignature(key, buffer)
                }
                val authHeader = "$uuid,$signature"
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", authHeader)
                    .build()
                return chain.proceed(newRequest)
            }
            return chain.proceed(request)
        }
    }
}
