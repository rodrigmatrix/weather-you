package com.rodrigmatrix.weatheryou

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.addlocation.di.AddLocationModule
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import com.rodrigmatrix.weatheryou.worker.UpdateWidgetWeatherDataWorker
import com.rodrigmatrix.weatheryou.home.di.HomeModule
import com.rodrigmatrix.weatheryou.locationdetails.di.LocationDetailsModule
import com.rodrigmatrix.weatheryou.presentation.di.WeatherYouAppModules
import com.rodrigmatrix.weatheryou.settings.di.SettingsModule
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

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
            WeatherYouAppModules.loadModules()
        }
        initRemoteConfig()
        setAppTheme()
        initWidgetWorker()
        CurrentWeatherWidget().updateWidget(this)
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

    private fun initWidgetWorker() {
        val updateWidgetRequest =
            PeriodicWorkRequestBuilder<UpdateWidgetWeatherDataWorker>(2, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                )
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_widget_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            updateWidgetRequest,
        )
    }

    private fun setAppTheme() {
        mainScope.launch {
            get<AppThemeManager>().setAppTheme()
        }
    }
}