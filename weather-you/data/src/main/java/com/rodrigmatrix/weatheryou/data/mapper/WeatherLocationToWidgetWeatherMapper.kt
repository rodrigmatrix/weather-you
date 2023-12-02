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
    weatherIcons = weatherIcons,
    currentCondition = currentWeatherDescription,
    maxWeather = maxTemperature,
    minWeather = lowestTemperature,
    lastUpdate = "",
    hours = if (hours.size >= 4) {
        listOf(
            WidgetWeatherHour(
                weather = hours[0].temperature,
                condition = hours[0].weatherCondition,
                weatherIcons = hours[0].weatherIcons,
            ),
            WidgetWeatherHour(
                weather = hours[1].temperature,
                condition = hours[1].weatherCondition,
                weatherIcons = hours[1].weatherIcons,
            ),
            WidgetWeatherHour(
                weather = hours[2].temperature,
                condition = hours[2].weatherCondition,
                weatherIcons = hours[2].weatherIcons,
            ),
            WidgetWeatherHour(
                weather = hours[3].temperature,
                condition = hours[3].weatherCondition,
                weatherIcons = hours[3].weatherIcons,
            ),
        )
    } else { emptyList() },
    days = if (days.size >= 2) {
        listOf(
            WidgetWeatherDay(
                maxWeather = days[0].maxTemperature,
                minWeather = days[0].minTemperature,
                condition = days[0].weatherCondition,
                weatherIcons = days[0].weatherIcons,
            ),
            WidgetWeatherDay(
                maxWeather = days[1].maxTemperature,
                minWeather = days[1].minTemperature,
                condition = days[1].weatherCondition,
                weatherIcons = days[1].weatherIcons,
            ),
        )
    } else { emptyList() },
)