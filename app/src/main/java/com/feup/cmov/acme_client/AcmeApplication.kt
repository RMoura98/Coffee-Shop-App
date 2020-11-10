package com.feup.cmov.acme_client

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AcmeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        preferences = getSharedPreferences("AcmeClientApplication", Context.MODE_PRIVATE)
    }

    companion object {
        private lateinit var context: Context
        private lateinit var preferences: SharedPreferences

        fun getAppContext(): Context {
            return context
        }

        fun getPreferences(): SharedPreferences {
            return preferences
        }
    }

}