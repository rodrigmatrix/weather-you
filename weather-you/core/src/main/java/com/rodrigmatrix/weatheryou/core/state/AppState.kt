package com.rodrigmatrix.weatheryou.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

val LocalWeatherYouAppSettings = compositionLocalOf {
    AppSettings(
        temperaturePreference = TemperaturePreference.METRIC,
        appThemePreference = AppThemePreference.SYSTEM_DEFAULT,
        appColorPreference = AppColorPreference.DEFAULT,
        enableWeatherAnimations = false,
        enableThemeColorWithWeatherAnimations = false,
    )
}

val LocalWeatherYouCurrentDestination = compositionLocalOf {
    ""
}

@Composable
fun WeatherYouAppState(
    appSettings: AppSettings,
    currentDestination: String,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWeatherYouAppSettings provides appSettings,
        LocalWeatherYouCurrentDestination provides currentDestination,
    ) {
        content()
    }
}

object WeatherYouAppState {

    val appSettings: AppSettings
        @Composable
        get() = LocalWeatherYouAppSettings.current

    val currentDestination: String
        @Composable
        get() = LocalWeatherYouCurrentDestination.current
}