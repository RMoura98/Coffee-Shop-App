package com.feup.cmov.acme_client.modules

import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
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
import java.lang.annotation.Documented
import javax.inject.Singleton
import javax.xml.transform.OutputKeys.METHOD


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
            .baseUrl("http://10.0.2.2:3001/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WebService::class.java)
    }


    class AuthenticatedRequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val request: Request = chain.request()
            val response: Response = chain.proceed(request)
            val invocation: Invocation? = request.tag(Invocation::class.java)
            val AuthenticatedRequest = invocation?.method()?.getAnnotation(WebService.Companion.AuthenticatedRequest::class.java)
            if (AuthenticatedRequest != null){
                val preferences = AcmeApplication.getPreferences()
                val userName = preferences.getString(
                    AcmeApplication.getAppContext().getString(R.string.preferences_userName), null
                )
                //
            }
            return response
        }
    }
}
