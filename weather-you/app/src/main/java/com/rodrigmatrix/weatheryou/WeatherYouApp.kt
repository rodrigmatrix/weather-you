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
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.widgets.weather.small.CurrentWeatherSmallWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class WeatherYouApp: Application() {

    private val mainScope: CoroutineScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherYouApp)
            WeatherYouDataModules.loadModules()
            HomeModule.loadModules()
            AddLocationModule.loadModules()
            LocationDetailsModule.loadModules()
            SettingsModule.loadModules()
        }
        initRemoteConfig()
        setAppTheme()
        CurrentWeatherSmallWidget().updateWidget()
    }

    private fun initRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
            fetchTimeoutInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
    }

    private fun setAppTheme() {
        mainScope.launch {
            get<AppThemeManager>().setAppTheme()
        }
    }
}