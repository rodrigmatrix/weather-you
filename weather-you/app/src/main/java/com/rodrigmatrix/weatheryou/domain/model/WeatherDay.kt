package com.rodrigmatrix.weatheryou.domain.model

data class WeatherDay(
    val dateTime: String,
    val weatherCondition: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherIcons: WeatherIcons,
    val hours: List<WeatherHour>,
    val precipitationProbability: Double,
    val precipitationType: String,
    val windSpeed: Double,
    val humidity: Double,
    val sunrise: String,
    val sunset: String
)