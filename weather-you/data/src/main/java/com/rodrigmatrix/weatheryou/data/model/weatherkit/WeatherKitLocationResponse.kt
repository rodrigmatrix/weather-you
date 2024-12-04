package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherKitLocationResponse(
    @SerialName("currentWeather")
    val currentWeather: CurrentWeather? = CurrentWeather(),
    @SerialName("forecastDaily")
    val forecastDaily: ForecastDaily? = ForecastDaily(),
    @SerialName("forecastHourly")
    val forecastHourly: ForecastHourly? = ForecastHourly(),
    @SerialName("forecastNextHour")
    val forecastNextHour: ForecastNextHour? = ForecastNextHour(),
)