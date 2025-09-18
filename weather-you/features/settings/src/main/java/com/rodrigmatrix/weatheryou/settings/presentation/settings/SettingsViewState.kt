package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.AppSettings

data class SettingsViewState(
    val appSettings: AppSettings = AppSettings.DEFAULT,
    val dialogState: SettingsDialogState = SettingsDialogState.HIDDEN,
    val hasBackgroundLocationPermission: Boolean = true,
    val hasLocationPermission: Boolean = true,
): ViewState

sealed interface SettingsDialogState {
    data object HIDDEN : SettingsDialogState
    data object TemperatureUnit : SettingsDialogState
    data object WindSpeedUnit : SettingsDialogState
    data object PrecipitationUnit : SettingsDialogState
    data object DistanceUnit : SettingsDialogState
    data object THEME : SettingsDialogState
    data object BackgroundLocation : SettingsDialogState
}
