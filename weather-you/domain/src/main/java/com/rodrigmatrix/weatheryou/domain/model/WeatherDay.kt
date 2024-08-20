package com.rodrigmatrix.weatheryou.domain.model

import org.joda.time.DateTime

data class WeatherDay(
    val dateTime: DateTime,
    val weatherCondition: WeatherCondition,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val hours: List<WeatherHour>,
    val precipitationProbability: Double,
    val precipitationType: String,
    val windSpeed: Double,
    val humidity: Double,
    val sunrise: DateTime,
    val sunset: DateTime,
    val unit: TemperaturePreference,
)