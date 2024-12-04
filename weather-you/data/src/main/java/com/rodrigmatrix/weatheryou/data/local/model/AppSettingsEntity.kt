package com.rodrigmatrix.weatheryou.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsEntity(
    val temperaturePreference: String = "",
    val appThemePreference: String = "",
    val appColorPreference: String = "",
    val enableWeatherAnimations: Boolean = false,
    val enableThemeColorWithWeatherAnimations: Boolean = false,
)