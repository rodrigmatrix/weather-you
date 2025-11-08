package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.lifecycle.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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
        type: ConditionType? = null,
        temperatureType: TemperatureType? = null,
    ) {
        _viewState.value = _viewState.value.copy(
            weatherLocation = weatherLocation,
            day = day,
            isCurrentDay = weatherLocation.days.indexOf(day) == 0,
            type = type ?: _viewState.value.type,
            temperatureType = temperatureType ?: _viewState.value.temperatureType,
        )
    }

    fun onTypeChange(type: ConditionType) {
        _viewState.update { viewState ->
            viewState.copy(
                type = type,
            )
        }
    }

    fun onTemperatureTypeChange(temperatureType: TemperatureType) {
        _viewState.value = _viewState.value.copy(
            temperatureType = temperatureType,
        )
    }
}