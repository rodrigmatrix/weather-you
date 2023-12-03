package com.rodrigmatrix.weatheryou.presentation.widget

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class CurrentWeatherWidgetConfigurationUiState(
    val locationsList: List<WeatherLocation> = emptyList(),
    val isLoading: Boolean = false,
    val selectedLocation: WeatherLocation? = null,
) : ViewState