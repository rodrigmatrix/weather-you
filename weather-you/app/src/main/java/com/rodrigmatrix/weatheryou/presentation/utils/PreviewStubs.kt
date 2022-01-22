package com.rodrigmatrix.weatheryou.presentation.utils

import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

val PreviewWeatherList = listOf(
    WeatherLocation(
        name = "Toronto",
        currentWeather = 1,
        currentWeatherIcon = R.raw.weather_thunder,
        currentTime = "11:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10,
        currentWeatherIcon = R.raw.weather_partly_shower,
        currentTime = "10:00 PM"
    )
)