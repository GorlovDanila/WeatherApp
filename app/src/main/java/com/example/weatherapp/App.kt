package com.example.weatherapp

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.weatherapp.di.DataContainer
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DataContainer.provideFusedLocation(applicationContext = this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}