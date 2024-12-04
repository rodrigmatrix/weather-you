package com.rodrigmatrix.weatheryou.domain.model

import org.joda.time.DateTime

data class WeatherHour(
    val temperature: Double,
    val dateTime: DateTime,
    val isDaylight: Boolean = true,
    val weatherCondition: WeatherCondition,
    val precipitationProbability: Double,
    val feelsLike: Double,
    val cloudCover: Double,
    val precipitationType: String,
    val humidity: Double,
    val visibility: Double,
    val precipitationAmount: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val uvIndex: Double,
    val snowfallIntensity: Double,
)