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

            Log.e("WebServiceModule", "Tried to intercept.")
            val request: Request = chain.request()
            val invocation: Invocation? = request.tag(Invocation::class.java)
            val authenticatedRequest = invocation?.method()?.getAnnotation(WebService.Companion.AuthenticatedRequest::class.java)
            val requestBody = request.body()?: return chain.proceed(request)
            if (authenticatedRequest != null){
                val (userName, uuid) = PreferencesUtils.getLoggedInUser()
                if (userName == null || uuid == null) return chain.proceed(request)
                val key = Security.retrieveRsaPrivateKey(userName)

                if(request.method() == "GET") {
                    val buffer = okio.Buffer()
                    buffer.write(uuid.toByteArray())
                    val signature = Security.makeSignature(key, buffer)
                    val authHeader = encodeToString("$uuid,$signature".toByteArray(), Base64.NO_WRAP)
                    val newRequest = request.newBuilder()
                        .addHeader("Authorization", authHeader)
                        .build()
                    return chain.proceed(newRequest)
                }
                else { // POST, etc
                    val buffer = okio.Buffer()
                    requestBody.writeTo(buffer)
                    buffer.write(",".toByteArray())
                    buffer.write(uuid.toByteArray())
                    val signature = Security.makeSignature(key, buffer)
                    val authHeader = encodeToString("$uuid,$signature".toByteArray(), Base64.NO_WRAP)
                    val newRequest = request.newBuilder()
                        .addHeader("Authorization", authHeader)
                        .build()
                    return chain.proceed(newRequest)
                }
            }
            return chain.proceed(request)
        }
    }
}
