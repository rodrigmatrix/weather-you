package com.rodrigmatrix.weatheryou.domain.model

data class WidgetWeather(
    val id: Int,
    val name: String,
    val lat: Double,
    val long: Double,
    val currentWeather: Double,
    val currentCondition: WeatherCondition,
    val maxWeather: Double,
    val minWeather: Double,
    val lastUpdate: String,
    val hours: List<WidgetWeatherHour>,
    val days: List<WidgetWeatherDay>,
)

data class WidgetWeatherHour(
    val weather: Double,
    val condition: WeatherCondition,
)

data class WidgetWeatherDay(
    val maxWeather: Double,
    val minWeather: Double,
    val condition: WeatherCondition,
)