package com.rodrigmatrix.weatheryou.domain.model

data class AppSettings(
    val temperaturePreference: TemperaturePreference,
    val appThemePreference: AppThemePreference,
    val appColorPreference: AppColorPreference,
    val enableWeatherAnimations: Boolean,
    val enableThemeColorWithWeatherAnimations: Boolean,
    val windUnitPreference: WindUnitPreference,
    val precipitationUnitPreference: PrecipitationUnitPreference,
    val distanceUnitPreference: DistanceUnitPreference,
) {
    companion object {
        val DEFAULT = AppSettings(
            temperaturePreference = TemperaturePreference.METRIC,
            appThemePreference = AppThemePreference.SYSTEM_DEFAULT,
            appColorPreference = AppColorPreference.DEFAULT,
            enableWeatherAnimations = false,
            enableThemeColorWithWeatherAnimations = false,
            windUnitPreference = WindUnitPreference.KPH,
            precipitationUnitPreference = PrecipitationUnitPreference.MM_CM,
            distanceUnitPreference = DistanceUnitPreference.KM,
        )
    }
}