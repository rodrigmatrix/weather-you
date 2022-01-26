package com.rodrigmatrix.weatheryou

import android.app.Application
import com.rodrigmatrix.weatheryou.data.di.dataModule
import com.rodrigmatrix.weatheryou.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class WeatherYouApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherYouApp)
            loadKoinModules(dataModule + presentationModule)
        }
    }
}