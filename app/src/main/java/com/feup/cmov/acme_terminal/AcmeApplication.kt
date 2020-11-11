package com.feup.cmov.acme_terminal

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AcmeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        preferences = getSharedPreferences("AcmeTerminalApplication", Context.MODE_PRIVATE)
    }

    companion object {
        private lateinit var context: Context
        private lateinit var preferences: SharedPreferences
        lateinit var userSignature: String
        lateinit var userId: String

        fun getAppContext(): Context {
            return context
        }

        fun getPreferences(): SharedPreferences {
            return preferences
        }
    }
}