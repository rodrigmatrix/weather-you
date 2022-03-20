package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel

class SettingsViewModel(

): com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel<SettingsViewState, SettingsViewEffect>(
    SettingsViewState()
) {

    init {
        loadUnits()
    }

    private fun loadUnits() {
        setState { it.copy(unitsList = listOf(
            "C° km/h",
            "F° mph",
            "C° mph"
        )) }
    }

    fun onEditUnits() {
        setState { it.copy(unitsDialogVisible = true) }
    }

    fun onNewUnit(newUnit: String) {
        setState { it.copy(unitsDialogVisible = false) }
    }

    fun onDismissDialog() {
        setState { it.copy(unitsDialogVisible = false) }
    }
}