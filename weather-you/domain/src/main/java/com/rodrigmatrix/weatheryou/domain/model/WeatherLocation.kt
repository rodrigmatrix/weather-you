package com.rodrigmatrix.weatheryou.domain.model

import org.joda.time.DateTime

data class WeatherLocation(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean,
    val currentWeather: Double,
    val currentCondition: WeatherCondition,
    val maxTemperature: Double,
    val lowestTemperature: Double,
    val feelsLike: Double,
    val currentTime: DateTime,
    val timeZone: String,
    val precipitationProbability: Double,
    val precipitationType: String,
    val humidity: Double,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val uvIndex: Double,
    val sunrise: DateTime,
    val sunset: DateTime,
    val visibility: Double,
    val pressure: Double,
    val days: List<WeatherDay> = emptyList(),
    val hours: List<WeatherHour> = emptyList(),
    val unit: TemperaturePreference,
)