package com.feup.cmov.acme_client

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AcmeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private lateinit var context: Context

        fun getAppContext(): Context {
            return AcmeApplication.context
        }
    }

}