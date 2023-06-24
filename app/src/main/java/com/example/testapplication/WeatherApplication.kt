package com.example.testapplication

import android.app.Application
import com.example.testapplication.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            // Reference Android context
            androidContext(this@WeatherApplication)
            modules(presentationModule)
        }
    }
}