package com.feup.cmov.acme_terminal.modules

import com.feup.cmov.acme_terminal.AcmeApplication
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.network.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
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
        val client = builder.build()

        return Retrofit.Builder()
                .baseUrl(AcmeApplication.getAppContext().getString(R.string.serverURL) + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(WebService::class.java)
    }
}