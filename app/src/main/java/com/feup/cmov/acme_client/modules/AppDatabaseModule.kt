package com.feup.cmov.acme_client.modules

import android.content.Context
import androidx.room.Room
import com.feup.cmov.acme_client.database.AppDatabase
import com.feup.cmov.acme_client.database.AppDatabaseDao
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabaseDao(@ApplicationContext appContext: Context): AppDatabaseDao {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "acme_database"
        )
        .fallbackToDestructiveMigration()
        .build().appDatabaseDao
    }

}
