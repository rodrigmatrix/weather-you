package com.rodrigmatrix.weatheryou.domain.model

data class WeatherHour(
    val temperature: Double,
    val dateTime: Long,
    val weatherCondition: WeatherCondition,
    val precipitationProbability: Double,
    val precipitationType: String
)