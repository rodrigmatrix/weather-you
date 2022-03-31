package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption

data class SettingsViewState(
    val selectedTemperature: TemperaturePreferenceOption = TemperaturePreferenceOption.METRIC,
    val appTheme: AppThemePreferenceOption = AppThemePreferenceOption.SYSTEM_DEFAULT,
    val unitsDialogVisible: Boolean = false,
    val themeDialogVisible: Boolean = false
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
