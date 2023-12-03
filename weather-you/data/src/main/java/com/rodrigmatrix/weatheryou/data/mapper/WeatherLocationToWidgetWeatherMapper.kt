package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherHour

fun WeatherLocation.toWidgetWeather(): WidgetWeather = WidgetWeather(
    id = id,
    name = name,
    lat = latitude,
    long = longitude,
    currentWeather = currentWeather,
    currentCondition = currentCondition,
    maxWeather = maxTemperature,
    minWeather = lowestTemperature,
    lastUpdate = "",
    hours = if (hours.size >= 4) {
        listOf(
            WidgetWeatherHour(
                weather = hours[0].temperature,
                condition = hours[0].weatherCondition,
            ),
            WidgetWeatherHour(
                weather = hours[1].temperature,
                condition = hours[1].weatherCondition,
            ),
            WidgetWeatherHour(
                weather = hours[2].temperature,
                condition = hours[2].weatherCondition,
            ),
            WidgetWeatherHour(
                weather = hours[3].temperature,
                condition = hours[3].weatherCondition,
            ),
        )
    } else { emptyList() },
    days = if (days.size >= 2) {
        listOf(
            WidgetWeatherDay(
                maxWeather = days[0].maxTemperature,
                minWeather = days[0].minTemperature,
                condition = days[0].weatherCondition,
            ),
            WidgetWeatherDay(
                maxWeather = days[1].maxTemperature,
                minWeather = days[1].minTemperature,
                condition = days[1].weatherCondition,
            ),
        )
    } else { emptyList() },
)