package com.rodrigmatrix.weatheryou.presentation.widget

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed interface CurrentWeatherWidgetConfigurationUiAction : ViewEffect {

    data object OnConfigurationCompleted : CurrentWeatherWidgetConfigurationUiAction
}