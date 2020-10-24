package com.feup.cmov.acme_client.modules

import com.feup.cmov.acme_client.network.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object WebServiceModule {

    @Provides
    @Singleton
    fun provideWebService(): WebService {
        return Retrofit.Builder()
            .baseUrl(" https://localhost:8080")
            .build()
            .create(WebService::class.java)
    }
}
