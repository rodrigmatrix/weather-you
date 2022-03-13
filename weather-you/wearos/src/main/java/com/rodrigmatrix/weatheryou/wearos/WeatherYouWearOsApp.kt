package com.rodrigmatrix.weatheryou.wearos

import android.app.Application
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouWearOsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherYouWearOsApp)
            WeatherYouDataModules.loadModules()
        }
    }
}