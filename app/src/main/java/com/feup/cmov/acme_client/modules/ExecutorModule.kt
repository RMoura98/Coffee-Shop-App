package com.feup.cmov.acme_client.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ExecutorModule {

    @Provides
    @Singleton
    fun provideExecutor(): Executor {
        return Executor{}
    }
}
