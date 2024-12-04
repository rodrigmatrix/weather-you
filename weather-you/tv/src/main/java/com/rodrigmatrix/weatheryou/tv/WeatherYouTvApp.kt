package com.rodrigmatrix.weatheryou.tv

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.addlocation.di.AddLocationModule
import com.rodrigmatrix.weatheryou.ads.di.WeatherYouAdsModule
import com.rodrigmatrix.weatheryou.data.BuildConfig
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.tv.di.WeatherYouTvModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouTvApp : Application() {

    private val mainScope: CoroutineScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
        startKoin {
            androidContext(this@WeatherYouTvApp)
            WeatherYouDataModules.loadModules()
            AddLocationModule.loadModules()
            WeatherYouTvModule.loadModules()
            WeatherYouAdsModule.loadModules()
        }
        setAppTheme()
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

    private fun setAppTheme() {
        mainScope.launch {
            get<AppThemeManager>().setAppTheme()
        }
    }
}