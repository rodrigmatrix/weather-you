package com.rodrigmatrix.weatheryou.domain.model

data class WeatherLocation(
    val name: String,
    val currentWeather: Double,
    val currentWeatherDescription: String,
    val maxTemperature: Double,
    val lowestTemperature: Double,
    val feelsLike: Double,
    val currentWeatherIcon: Int,
    val currentTime: String,
    val days: List<Day> = emptyList(),
    val hours: List<Hour> = emptyList(),
    val timeZone: String
)