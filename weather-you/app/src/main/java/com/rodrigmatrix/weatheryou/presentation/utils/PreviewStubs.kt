package com.rodrigmatrix.weatheryou.presentation.utils

import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.Day
import com.rodrigmatrix.weatheryou.domain.model.Hour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

val PreviewWeatherLocation = WeatherLocation(
    name = "Toronto",
    currentWeather = 10.0,
    feelsLike = -2.0,
    maxTemperature = 10.0,
    lowestTemperature = 0.0,
    currentWeatherDescription = "Snow",
    currentWeatherIcon = R.raw.weather_snow_sunny,
    currentTime = "11:00 PM"
)

val PreviewWeatherList = listOf(
    WeatherLocation(
        name = "Toronto",
        currentWeather = 1.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_snow_sunny,
        currentTime = "11:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_stormshowersday,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_snow,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_mist,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_thunder,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_sunny,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_night,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_partly_cloudy,
        currentTime = "10:00 PM"
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_rainynight,
        currentTime = "10:00 PM"
    )
)

val PreviewHourlyForecast = listOf(
    Hour(
        temperature = 1.0,
        icon = R.raw.weather_rainynight,
        time = "Now"
    ),
    Hour(
        temperature = 9.0,
        icon = R.raw.weather_night,
        time = "6AM"
    ),
    Hour(
        temperature = 9.0,
        icon = R.raw.weather_partly_cloudy,
        time = "7AM"
    ),
    Hour(
        temperature = 8.0,
        icon = R.raw.weather_thunder,
        time = "8AM"
    ),
    Hour(
        temperature = 6.0,
        icon = R.raw.weather_rainynight,
        time = "9AM"
    ),
    Hour(
        temperature = 6.0,
        icon = R.raw.weather_rainynight,
        time = "11AM"
    ),
    Hour(
        temperature = 5.0,
        icon = R.raw.weather_stormshowersday,
        time = "12PM"
    ),
    Hour(
        temperature = 2.0,
        icon = R.raw.weather_rainynight,
        time = "1PM"
    )
)

val PreviewFutureDaysForecast = listOf(
    Day(
        dateTime = "Today",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_stormshowersday,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Tomorrow",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_rainynight,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_stormshowersday,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_snow,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_snow_sunny,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_snownight,
        hours = PreviewHourlyForecast
    ),
    Day(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_sunny,
        hours = PreviewHourlyForecast
    )
)