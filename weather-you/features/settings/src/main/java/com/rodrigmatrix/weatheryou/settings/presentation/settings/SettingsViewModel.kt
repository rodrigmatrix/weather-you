package com.rodrigmatrix.weatheryou.settings.presentation.settings

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
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
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<SettingsViewState, SettingsViewEffect>(SettingsViewState()) {

    init {
        loadUnits()
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

    fun onEditUnits() {
        setState { it.copy(unitsDialogVisible = true) }
    }

    fun onNewUnit(newUnit: TemperaturePreferenceOption) {
        viewModelScope.launch {
            setTemperaturePreferenceUseCase(newUnit.option)
                .flowOn(coroutineDispatcher)
                .catch {  }
                .onCompletion {
                    loadUnits()
                    hideDialogs()
                }
                .collect()
        }
    }

    private fun TemperaturePreference.toOption(): TemperaturePreferenceOption {
        return TemperaturePreferenceOption.values().find {
            it.option == this
        } ?: TemperaturePreferenceOption.METRIC
    }

    fun hideDialogs() {
        setState {
            it.copy(unitsDialogVisible = false)
        }
    }
}