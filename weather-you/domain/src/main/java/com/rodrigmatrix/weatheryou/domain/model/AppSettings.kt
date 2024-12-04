package com.rodrigmatrix.weatheryou.domain.model

data class AppSettings(
    val temperaturePreference: TemperaturePreference,
    val appThemePreference: AppThemePreference,
    val appColorPreference: AppColorPreference,
    val enableWeatherAnimations: Boolean,
    val enableThemeColorWithWeatherAnimations: Boolean,
)