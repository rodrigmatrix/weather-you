package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption

data class SettingsViewState(
    val appSettings: AppSettings = AppSettings(
        temperaturePreference = TemperaturePreference.METRIC,
        appThemePreference = AppThemePreference.SYSTEM_DEFAULT,
        appColorPreference = AppColorPreference.DEFAULT,
        enableWeatherAnimations = false,
        enableThemeColorWithWeatherAnimations = false,
    ),
    val dialogState: SettingsDialogState = SettingsDialogState.HIDDEN,
    val hasBackgroundLocationPermission: Boolean = true,
    val hasLocationPermission: Boolean = true,
): ViewState

sealed interface SettingsDialogState {
    data object HIDDEN : SettingsDialogState
    data object UNITS : SettingsDialogState
    data object THEME : SettingsDialogState
    data object BackgroundLocation : SettingsDialogState
}
