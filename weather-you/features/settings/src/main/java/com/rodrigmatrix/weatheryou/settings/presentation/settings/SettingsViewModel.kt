package com.rodrigmatrix.weatheryou.settings.presentation.settings

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.extensions.catch
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getTemperaturePreferenceUseCase: GetTemperaturePreferenceUseCase,
    private val setTemperaturePreferenceUseCase: SetTemperaturePreferenceUseCase,
    private val getAppThemePreferenceUseCase: GetAppThemePreferenceUseCase,
    private val setAppThemePreferenceUseCase: SetAppThemePreferenceUseCase,
    private val appThemeManager: AppThemeManager,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<SettingsViewState, SettingsViewEffect>(SettingsViewState()) {

    init {
        loadUnits()
        loadThemes()
    }

    private fun loadUnits() {
        viewModelScope.launch {
            getTemperaturePreferenceUseCase()
                .flowOn(coroutineDispatcher)
                .catch {  }
                .collect { temperaturePreference ->
                    setState { it.copy(selectedTemperature = temperaturePreference.toOption()) }
                }
        }
    }

    private fun loadThemes() {
        viewModelScope.launch {
            getAppThemePreferenceUseCase()
                .flowOn(coroutineDispatcher)
                .catch {  }
                .collect { themePreference ->
                    setState { it.copy(selectedAppTheme = themePreference.toOption()) }
                }
        }
    }

    fun onEditUnit() {
        setState { it.copy(unitsDialogVisible = true) }
    }

    fun onEditTheme() {
        setState { it.copy(themeDialogVisible = true) }
    }

    fun onNewUnit(newUnit: TemperaturePreferenceOption) {
        viewModelScope.launch {
            setTemperaturePreferenceUseCase(newUnit.option)
                .flowOn(coroutineDispatcher)
                .catch()
                .onCompletion {
                    loadUnits()
                    hideDialogs()
                }
                .collect()
        }
    }

    fun onNewTheme(newTheme: AppThemePreferenceOption) {
        viewModelScope.launch {
            setAppThemePreferenceUseCase(newTheme.option)
                .flowOn(coroutineDispatcher)
                .catch()
                .onCompletion {
                    loadThemes()
                    hideDialogs()
                    appThemeManager.setAppTheme()
                }
                .collect()
        }
    }

    private fun TemperaturePreference.toOption(): TemperaturePreferenceOption {
        return TemperaturePreferenceOption.values().find {
            it.option == this
        } ?: TemperaturePreferenceOption.METRIC
    }

    private fun AppThemePreference.toOption(): AppThemePreferenceOption {
        return AppThemePreferenceOption.values().find {
            it.option == this
        } ?: AppThemePreferenceOption.SYSTEM_DEFAULT
    }

    fun hideDialogs() {
        setState {
            it.copy(unitsDialogVisible = false, themeDialogVisible = false)
        }
    }
}