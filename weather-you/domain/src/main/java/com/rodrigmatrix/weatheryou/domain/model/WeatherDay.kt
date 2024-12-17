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
    val precipitationAmount: Double,
    val snowfallAmount: Double,
    val solarMidnight: DateTime,
    val solarNoon: DateTime,
    val moonPhase: MoonPhase,
    val sunriseAstronomical: DateTime,
    val sunsetAstronomical: DateTime,
    val sunriseNautical: DateTime,
    val sunsetNautical: DateTime,
    val sunriseCivil: DateTime,
    val sunsetCivil: DateTime,
    val precipitationType: PrecipitationType,
    val windSpeed: Double,
    val humidity: Double,
    val sunrise: DateTime,
    val sunset: DateTime,
)