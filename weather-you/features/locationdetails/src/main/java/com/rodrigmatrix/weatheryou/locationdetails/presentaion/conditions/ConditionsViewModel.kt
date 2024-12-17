package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.lifecycle.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConditionsViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(ConditionsViewState())
    val viewState: StateFlow<ConditionsViewState> = _viewState

    fun hideConditions() {
        _viewState.value = _viewState.value.copy(
            weatherLocation = null,
            day = null,
            temperatureType = TemperatureType.Actual,
        )
    }

    fun setConditions(
        weatherLocation: WeatherLocation,
        day: WeatherDay,
    ) {
        _viewState.value = _viewState.value.copy(
            weatherLocation = weatherLocation,
            day = day,
            isCurrentDay = weatherLocation.days.indexOf(day) == 0,
        )
    }

    fun onTemperatureTypeChange(temperatureType: TemperatureType) {
        _viewState.value = _viewState.value.copy(
            temperatureType = temperatureType,
        )
    }
}