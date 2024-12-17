package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class ConditionsViewState(
    val weatherLocation: WeatherLocation? = null,
    val day: WeatherDay? = null,
    val isCurrentDay: Boolean = false,
    val temperatureType: TemperatureType = TemperatureType.Actual,
)

enum class TemperatureType {
    Actual,
    FeelsLike,
}