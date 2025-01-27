package com.rodrigmatrix.weatheryou.wearos.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

data class SettingsViewState(
    val appSettings: AppSettings = AppSettings(
        temperaturePreference = TemperaturePreference.METRIC,
        appThemePreference = AppThemePreference.SYSTEM_DEFAULT,
        appColorPreference = AppColorPreference.DEFAULT,
        enableWeatherAnimations = true,
        enableThemeColorWithWeatherAnimations = false,
    ),
) : ViewState