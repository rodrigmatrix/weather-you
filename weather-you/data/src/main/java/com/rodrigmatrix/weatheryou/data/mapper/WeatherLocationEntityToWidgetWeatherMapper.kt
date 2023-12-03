package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherHour

fun WeatherWidgetLocationEntity.toWidgetWeather(): WidgetWeather = WidgetWeather(
    id = id,
    name = name,
    lat = latitude,
    long = longitude,
    currentWeather = currentWeather,
    currentCondition = WeatherCondition.valueOf(currentCondition),
    maxWeather = maxWeather,
    minWeather = minWeather,
    lastUpdate = lastUpdate,
    hours = listOf(
        WidgetWeatherHour(
            weather = nextHourWeather,
            condition = WeatherCondition.valueOf(nextHourCondition),
        ),
        WidgetWeatherHour(
            weather = nextTwoHoursWeather,
            condition = WeatherCondition.valueOf(nextTwoHoursCondition),
        ),
        WidgetWeatherHour(
            weather = nextThreeHoursWeather,
            condition = WeatherCondition.valueOf(nextThreeHoursCondition),
        ),
        WidgetWeatherHour(
            weather = nextFourHoursWeather,
            condition = WeatherCondition.valueOf(nextFourHoursCondition),
        ),
    ),
    days = listOf(
        WidgetWeatherDay(
            maxWeather = tomorrowMaxWeather,
            minWeather = tomorrowMinWeather,
            condition = WeatherCondition.valueOf(tomorrowCondition),
        ),
        WidgetWeatherDay(
            maxWeather = nextTwoDaysMaxWeather,
            minWeather = nextTwoDaysMinWeather,
            condition = WeatherCondition.valueOf(nextTwoDaysCondition),
        ),
    ),
)