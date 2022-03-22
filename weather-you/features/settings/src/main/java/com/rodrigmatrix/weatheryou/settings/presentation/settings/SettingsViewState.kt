package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption

data class SettingsViewState(
    val selectedTemperature: TemperaturePreferenceOption = TemperaturePreferenceOption.METRIC,
    val unitsDialogVisible: Boolean = false,
    val themeDialogVisibile: Boolean = false
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
