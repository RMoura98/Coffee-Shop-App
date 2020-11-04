package com.feup.cmov.acme_client

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.feup.cmov.acme_client.databinding.ActivityMainBinding
import com.feup.cmov.acme_client.utils.Debug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // TODO: REMOVE THIS BEFORE TURNING IN PROJECT!
    @Inject
    lateinit var debug: Debug

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debug.startDebugMode()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        activity = this
    }

    companion object {
        private lateinit var activity: Activity

        fun getActivity(): Activity {
            return activity
        }
    }
}