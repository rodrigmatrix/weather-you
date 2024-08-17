package com.rodrigmatrix.weatheryou.tv

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.data.BuildConfig
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import com.rodrigmatrix.weatheryou.tv.di.WeatherYouTvModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouTvApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
        startKoin {
            androidContext(this@WeatherYouTvApp)
            WeatherYouDataModules.loadModules()
            WeatherYouTvModule.loadModules()
        }
    }

    private fun initRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
            fetchTimeoutInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
    }
}