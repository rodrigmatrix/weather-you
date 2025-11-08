package com.rodrigmatrix.weatheryou.wearos.presentation.settings

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppSettingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<SettingsViewState, SettingsViewEffect>(SettingsViewState()) {


    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            getAppSettingsUseCase().collect { appSettings ->
                setState {
                    it.copy(appSettings = appSettings)
                }
            }
        }
    }

//    fun onPermissionChanged() {
//        setEffect { SettingsViewEffect.OnPermissionChanged }
//    }
//
//    fun onEditUnit() {
//        setState { it.copy(dialogState = SettingsDialogState.UNITS) }
//    }
//
//    fun onEditTheme() {
//        setState { it.copy(dialogState = SettingsDialogState.THEME) }
//    }
//
//    fun onNewUnit(newUnit: TemperaturePreferenceOption) {
//        viewModelScope.launch {
//            val appSettings = viewState.value.appSettings.copy(
//                temperaturePreference = newUnit.option
//            )
//            setAppSettingsUseCase(appSettings)
//                .flowOn(coroutineDispatcher)
//                .catch()
//                .collect()
//        }
//    }
//
//    fun onWeatherAnimationsChange(enableWeatherAnimations: Boolean) {
//        viewModelScope.launch {
//            val appSettings = viewState.value.appSettings.copy(
//                enableWeatherAnimations = enableWeatherAnimations
//            )
//            setAppSettingsUseCase(appSettings)
//                .flowOn(coroutineDispatcher)
//                .catch()
//                .collect()
//        }
//    }
//
//    fun hideDialogs() {
//        setState {
//            it.copy(dialogState = SettingsDialogState.HIDDEN)
//        }
//    }
//
//    fun updateLocationsState(
//        hasLocationPermission: Boolean,
//        hasBackgroundLocationPermission: Boolean,
//    ) {
//        setState {
//            it.copy(
//                hasLocationPermission = hasLocationPermission,
//                hasBackgroundLocationPermission = hasBackgroundLocationPermission,
//            )
//        }
//    }
}