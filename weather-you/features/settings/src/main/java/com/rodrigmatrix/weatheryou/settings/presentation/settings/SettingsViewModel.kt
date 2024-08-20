package com.rodrigmatrix.weatheryou.settings.presentation.settings

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.extensions.catch
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.toPreference
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getTemperaturePreferenceUseCase: GetTemperaturePreferenceUseCase,
    private val setTemperaturePreferenceUseCase: SetTemperaturePreferenceUseCase,
    private val getAppThemePreferenceUseCase: GetAppThemePreferenceUseCase,
    private val setAppThemePreferenceUseCase: SetAppThemePreferenceUseCase,
    private val getAppColorPreferenceUseCase: GetAppColorPreferenceUseCase,
    private val setAppColorPreferenceUseCase: SetAppColorPreferenceUseCase,
    private val appThemeManager: AppThemeManager,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<SettingsViewState, SettingsViewEffect>(SettingsViewState()) {

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        combine(
            getTemperaturePreferenceUseCase(),
            getAppThemePreferenceUseCase(),
            getAppColorPreferenceUseCase(),
        ) { temperature, theme, color ->
            setState {
                it.copy(
                    selectedTemperature = temperature.toOption(),
                    selectedAppTheme = theme.toOption(),
                    selectedColor = color.toOption(),
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEditUnit() {
        setState { it.copy(dialogState = SettingsDialogState.UNITS) }
    }

    fun onEditTheme() {
        setState { it.copy(dialogState = SettingsDialogState.THEME) }
    }

    fun onNewUnit(newUnit: TemperaturePreferenceOption) {
        viewModelScope.launch {
            setTemperaturePreferenceUseCase(newUnit.option)
                .flowOn(coroutineDispatcher)
                .catch()
                .collect()
        }
    }

    fun onNewColorTheme(newColor: AppColorPreferenceOption) {
        viewModelScope.launch {
            setAppColorPreferenceUseCase(newColor.option.toPreference())
                .flowOn(coroutineDispatcher)
                .catch()
                .collect()
        }
    }

    fun onNewTheme(newTheme: AppThemePreferenceOption) {
        viewModelScope.launch {
            setAppThemePreferenceUseCase(newTheme.option.toPreference())
                .flowOn(coroutineDispatcher)
                .catch()
                .collect()
        }
    }

    private fun TemperaturePreference.toOption(): TemperaturePreferenceOption {
        return TemperaturePreferenceOption.entries.find {
            it.option == this
        } ?: TemperaturePreferenceOption.METRIC
    }

    private fun AppThemePreference.toOption(): AppThemePreferenceOption {
        return AppThemePreferenceOption.entries.find {
            it.option.toPreference() == this
        } ?: AppThemePreferenceOption.SYSTEM_DEFAULT
    }

    private fun AppColorPreference.toOption(): AppColorPreferenceOption {
        return AppColorPreferenceOption.entries.find {
            it.option.toPreference() == this
        } ?: AppColorPreferenceOption.DEFAULT
    }

    fun hideDialogs() {
        setState {
            it.copy(dialogState = SettingsDialogState.HIDDEN)
        }
    }
}