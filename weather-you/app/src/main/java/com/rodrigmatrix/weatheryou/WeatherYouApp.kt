package com.rodrigmatrix.weatheryou

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.addlocation.di.AddLocationModule
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import com.rodrigmatrix.weatheryou.home.di.HomeModule
import com.rodrigmatrix.weatheryou.locationdetails.di.LocationDetailsModule
import com.rodrigmatrix.weatheryou.settings.di.SettingsModule
import com.rodrigmatrix.weatheryou.widgets.weather.small.CurrentWeatherSmallWidget
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
        startKoin {
            androidContext(this@WeatherYouApp)
            WeatherYouDataModules.loadModules()
            HomeModule.loadModules()
            AddLocationModule.loadModules()
            LocationDetailsModule.loadModules()
            SettingsModule.loadModules()
        }
        CurrentWeatherSmallWidget().updateWidget()
    }

    private fun initRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
}