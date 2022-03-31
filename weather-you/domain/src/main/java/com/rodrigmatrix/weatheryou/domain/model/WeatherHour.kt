package com.rodrigmatrix.weatheryou.domain.model

data class WeatherHour(
    val temperature: Double,
    val weatherIcons: WeatherIcons,
    val dateTime: Long,
    val weatherCondition: String,
    val precipitationProbability: Double,
    val precipitationType: String
)