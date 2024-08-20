package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption

data class SettingsViewState(
    val selectedTemperature: TemperaturePreferenceOption = TemperaturePreferenceOption.METRIC,
    val selectedAppTheme: AppThemePreferenceOption = AppThemePreferenceOption.SYSTEM_DEFAULT,
    val selectedColor: AppColorPreferenceOption = AppColorPreferenceOption.DEFAULT,
    val dialogState: SettingsDialogState = SettingsDialogState.HIDDEN,
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState

sealed interface SettingsDialogState {
    data object HIDDEN : SettingsDialogState
    data object UNITS : SettingsDialogState
    data object THEME : SettingsDialogState
}
