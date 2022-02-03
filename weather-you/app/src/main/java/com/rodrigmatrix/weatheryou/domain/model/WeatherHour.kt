package com.rodrigmatrix.weatheryou.domain.model

data class WeatherHour(
    val temperature: Double,
    val weatherIcon: WeatherIcon,
    val dateTime: String,
    val weatherCondition: String,
    val precipitationProbability: Double,
    val precipitationType: String
)