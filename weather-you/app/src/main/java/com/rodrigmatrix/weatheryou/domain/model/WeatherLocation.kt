package com.rodrigmatrix.weatheryou.domain.model

data class WeatherLocation(
    val name: String,
    val currentWeather: Double,
    val currentWeatherIcon: Int,
    val currentTime: String,
    val days: List<Day> = emptyList()
)