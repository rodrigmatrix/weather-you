package com.rodrigmatrix.weatheryou.domain.model

data class WeatherLocation(
    val name: String,
    val currentWeather: Int,
    val currentWeatherIcon: Int,
    val currentTime: String
)