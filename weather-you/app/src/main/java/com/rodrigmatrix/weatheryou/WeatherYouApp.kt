package com.rodrigmatrix.weatheryou

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.di.WeatherYouModules
import com.rodrigmatrix.weatheryou.widgets.weather.small.CurrentWeatherSmallWidget
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
        startKoin {
            androidContext(this@WeatherYouApp)
            WeatherYouModules.loadModules()
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