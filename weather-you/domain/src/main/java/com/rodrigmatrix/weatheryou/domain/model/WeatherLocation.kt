package com.rodrigmatrix.weatheryou.domain.model

data class WeatherLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean,
    val currentWeather: Double,
    val currentWeatherDescription: String,
    val maxTemperature: Double,
    val lowestTemperature: Double,
    val feelsLike: Double,
    val weatherIcons: WeatherIcons,
    val currentTime: Long,
    val timeZone: String,
    val precipitationProbability: Double,
    val precipitationType: String,
    val humidity: Double,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val uvIndex: Double,
    val sunrise: Long,
    val sunset: Long,
    val visibility: Double,
    val pressure: Double,
    val days: List<WeatherDay> = emptyList(),
    val hours: List<WeatherHour> = emptyList()
)