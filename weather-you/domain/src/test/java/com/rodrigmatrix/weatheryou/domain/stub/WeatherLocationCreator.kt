package com.rodrigmatrix.weatheryou.domain.stub

import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

internal fun createWeatherLocation(
    id: Int = 0,
    name: String = "",
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    isCurrentLocation: Boolean = false,
    currentWeather: Double = 0.0,
    currentWeatherDescription: String = "",
    maxTemperature: Double = 0.0,
    lowestTemperature: Double = 0.0,
    feelsLike: Double = 0.0,
    weatherIcons: WeatherIcons = WeatherIcons(0, 0),
    currentTime: Long = 0L,
    timeZone: String = "",
    precipitationProbability: Double = 0.0,
    precipitationType: String = "",
    humidity: Double = 0.0,
    dewPoint: Double = 0.0,
    windSpeed: Double = 0.0,
    windDirection: Double = 0.0,
    uvIndex: Double = 0.0,
    sunrise: Long = 0L,
    sunset: Long = 0L,
    visibility: Double = 0.0,
    pressure: Double = 0.0,
    days: List<WeatherDay> = emptyList(),
    hours: List<WeatherHour> = emptyList()
): WeatherLocation {
    return WeatherLocation(
        id,
        name,
        latitude,
        longitude,
        isCurrentLocation,
        currentWeather,
        currentWeatherDescription,
        maxTemperature,
        lowestTemperature,
        feelsLike,
        weatherIcons,
        currentTime,
        timeZone,
        precipitationProbability,
        precipitationType,
        humidity,
        dewPoint,
        windSpeed,
        windDirection,
        uvIndex,
        sunrise,
        sunset,
        visibility,
        pressure,
        days,
        hours
    )
}