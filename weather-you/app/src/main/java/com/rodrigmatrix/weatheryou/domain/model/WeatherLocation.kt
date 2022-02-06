package com.rodrigmatrix.weatheryou.domain.model

data class WeatherLocation(
    val name: String,
    val currentWeather: Double,
    val currentWeatherDescription: String,
    val maxTemperature: Double,
    val lowestTemperature: Double,
    val feelsLike: Double,
    val weatherIcons: WeatherIcons,
    val currentTime: String,
    val days: List<WeatherDay> = emptyList(),
    val hours: List<WeatherHour> = emptyList(),
    val timeZone: String,
    val precipitationProbability: Double,
    val precipitationType: String
)