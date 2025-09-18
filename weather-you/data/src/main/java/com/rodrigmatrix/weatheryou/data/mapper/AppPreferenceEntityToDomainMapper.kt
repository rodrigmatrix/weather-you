package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.AppSettingsEntity
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference

fun AppSettingsEntity.mapToDomain(): AppSettings {
    val locale = java.util.Locale.getDefault()
    val country = locale.country
    val fahrenheitCountries = setOf("US", "BZ", "BS", "KY")
    val usesFahrenheit = fahrenheitCountries.contains(country)
    val imperialCountries = setOf("US", "LR", "MM")
    val usesImperial = imperialCountries.contains(country)

    return AppSettings(
        temperaturePreference = safeEnumValueOf(
            temperaturePreference,
            if (usesFahrenheit) TemperaturePreference.IMPERIAL else TemperaturePreference.METRIC
        ),
        windUnitPreference = safeEnumValueOf(
            windUnitPreference,
            if (usesImperial) WindUnitPreference.MPH else WindUnitPreference.KPH
        ),
        precipitationUnitPreference = safeEnumValueOf(
            precipitationUnitPreference,
            if (usesImperial) PrecipitationUnitPreference.IN else PrecipitationUnitPreference.MM_CM
        ),
        distanceUnitPreference = safeEnumValueOf(
            distanceUnitPreference,
            if (usesImperial) DistanceUnitPreference.MI else DistanceUnitPreference.KM
        ),
        appThemePreference = safeEnumValueOf(appThemePreference, AppThemePreference.SYSTEM_DEFAULT),
        appColorPreference = safeEnumValueOf(appColorPreference, AppColorPreference.DYNAMIC),
        enableWeatherAnimations = enableWeatherAnimations,
        enableThemeColorWithWeatherAnimations = enableThemeColorWithWeatherAnimations
    )
}

private inline fun <reified T : Enum<T>> safeEnumValueOf(name: String?, default: T): T {
    return try {
        if (!name.isNullOrEmpty()) enumValueOf<T>(name) else default
    } catch (_: IllegalArgumentException) {
        default
    }
}

fun AppSettings.mapToEntity() = AppSettingsEntity(
    temperaturePreference = temperaturePreference.name,
    appThemePreference = appThemePreference.name,
    appColorPreference = appColorPreference.name,
    enableWeatherAnimations = enableWeatherAnimations,
    enableThemeColorWithWeatherAnimations = enableThemeColorWithWeatherAnimations,
)