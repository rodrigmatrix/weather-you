package com.rodrigmatrix.weatheryou.domain.model

import org.joda.time.DateTime

data class WeatherHour(
    val temperature: Double,
    val dateTime: DateTime,
    val weatherCondition: WeatherCondition,
    val precipitationProbability: Double,
    val precipitationType: String
)