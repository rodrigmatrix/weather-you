package com.rodrigmatrix.weatheryou.tv

import android.app.Application
import com.rodrigmatrix.weatheryou.addlocation.di.AddLocationModule
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import com.rodrigmatrix.weatheryou.home.di.HomeModule
import com.rodrigmatrix.weatheryou.locationdetails.di.LocationDetailsModule
import com.rodrigmatrix.weatheryou.settings.di.SettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouTvApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherYouTvApp)
            WeatherYouDataModules.loadModules()
            HomeModule.loadModules()
            AddLocationModule.loadModules()
            LocationDetailsModule.loadModules()
            SettingsModule.loadModules()
        }
    }
}