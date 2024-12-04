package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.AppSettingsEntity
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

fun AppSettingsEntity.mapToDomain() = AppSettings(
    temperaturePreference = TemperaturePreference.valueOf(temperaturePreference),
    appThemePreference = AppThemePreference.valueOf(appThemePreference),
    appColorPreference = AppColorPreference.valueOf(appColorPreference),
    enableWeatherAnimations = enableWeatherAnimations,
    enableThemeColorWithWeatherAnimations = enableThemeColorWithWeatherAnimations,
)


fun AppSettings.mapToEntity() = AppSettingsEntity(
    temperaturePreference = temperaturePreference.name,
    appThemePreference = appThemePreference.name,
    appColorPreference = appColorPreference.name,
    enableWeatherAnimations = enableWeatherAnimations,
    enableThemeColorWithWeatherAnimations = enableThemeColorWithWeatherAnimations,
)