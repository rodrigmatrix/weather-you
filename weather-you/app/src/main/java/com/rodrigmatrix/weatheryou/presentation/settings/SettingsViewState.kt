package com.rodrigmatrix.weatheryou.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState

data class SettingsViewState(
    val selectedUnit: String = "C° km/h",
    val unitsList: List<String> = emptyList(),
    val unitsDialogVisible: Boolean = false
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
