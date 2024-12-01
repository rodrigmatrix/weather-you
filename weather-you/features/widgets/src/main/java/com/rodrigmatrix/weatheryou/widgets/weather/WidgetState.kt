package com.rodrigmatrix.weatheryou.widgets.weather

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

sealed interface WidgetState {
    data object Loading : WidgetState
    data class Complete(val weather: WeatherLocation) : WidgetState
    data object Error : WidgetState
}