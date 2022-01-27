package com.rodrigmatrix.weatheryou.presentation.utils

import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

val PreviewWeatherLocation = WeatherLocation(
    name = "Toronto",
    currentWeather = 10.0,
    feelsLike = -2.0,
    maxTemperature = 10.0,
    lowestTemperature = 0.0,
    currentWeatherDescription = "Snow",
    currentWeatherIcon = R.raw.weather_snow_sunny,
    currentTime = "11:00 PM",
    timeZone = "",
    precipitationProbability = 60.0,
    precipitationType = "snow"
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
        currentTime = "11:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_stormshowersday,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_snow,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_mist,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_thunder,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_sunny,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_night,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_partly_cloudy,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherLocation(
        name = "Montreal",
        currentWeather = -10.0,
        feelsLike = -2.0,
        maxTemperature = 10.0,
        lowestTemperature = 0.0,
        currentWeatherDescription = "Snow",
        currentWeatherIcon = R.raw.weather_rainynight,
        currentTime = "10:00 PM",
        timeZone = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    )
)

val PreviewHourlyForecast = listOf(
    WeatherHour(
        temperature = 1.0,
        icon = R.raw.weather_rainynight,
        dateTime = "Now",
        weatherCondition = "wd",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 9.0,
        icon = R.raw.weather_night,
        dateTime = "6AM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 9.0,
        icon = R.raw.weather_partly_cloudy,
        dateTime = "7AM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 8.0,
        icon = R.raw.weather_thunder,
        dateTime = "8AM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 6.0,
        icon = R.raw.weather_rainynight,
        dateTime = "9AM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 6.0,
        icon = R.raw.weather_rainynight,
        dateTime = "11AM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 5.0,
        icon = R.raw.weather_stormshowersday,
        dateTime = "12PM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    ),
    WeatherHour(
        temperature = 2.0,
        icon = R.raw.weather_rainynight,
        dateTime = "1PM",
        weatherCondition = "",
        precipitationProbability = 0.0,
        precipitationType = ""
    )
)

val PreviewFutureDaysForecast = listOf(
    WeatherDay(
        dateTime = "Today",
        weatherCondition = "Parcialmente nublado",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_stormshowersday,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Tomorrow",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_rainynight,
        hours = PreviewHourlyForecast,
        precipitationProbability = 80.0,
        precipitationType = "rain",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_stormshowersday,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_thunder,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    ),
    WeatherDay(
        dateTime = "Monday, Jan 7",
        weatherCondition = "Cloudy",
        temperature = 10.0,
        maxTemperature = 14.0,
        minTemperature = 1.0,
        icon = R.raw.weather_snow,
        hours = PreviewHourlyForecast,
        precipitationProbability = 0.0,
        precipitationType = "",
        windSpeed = 0.0,
        humidity = 0.0,
        sunrise = "7:00AM",
        sunset = "7:00AM",
    )
)