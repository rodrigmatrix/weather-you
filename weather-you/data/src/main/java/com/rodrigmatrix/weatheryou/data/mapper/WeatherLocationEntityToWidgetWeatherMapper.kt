package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeatherHour

fun WeatherWidgetLocationEntity.toWidgetWeather(iconsMapper: IconsMapper): WidgetWeather = WidgetWeather(
    id = id,
    name = name,
    lat = latitude,
    long = longitude,
    currentWeather = currentWeather,
    currentCondition = currentCondition,
    weatherIcons = iconsMapper.map(currentCondition),
    maxWeather = maxWeather,
    minWeather = minWeather,
    lastUpdate = lastUpdate,
    hours = listOf(
        WidgetWeatherHour(
            weather = nextHourWeather,
            condition = nextHourCondition,
            weatherIcons = iconsMapper.map(currentCondition),
        ),
        WidgetWeatherHour(
            weather = nextTwoHoursWeather,
            condition = nextTwoHoursCondition,
            weatherIcons = iconsMapper.map(nextTwoHoursCondition),
        ),
        WidgetWeatherHour(
            weather = nextThreeHoursWeather,
            condition = nextThreeHoursCondition,
            weatherIcons = iconsMapper.map(nextThreeHoursCondition),
        ),
        WidgetWeatherHour(
            weather = nextFourHoursWeather,
            condition = nextFourHoursCondition,
            weatherIcons = iconsMapper.map(nextFourHoursCondition),
        ),
    ),
    days = listOf(
        WidgetWeatherDay(
            maxWeather = tomorrowMaxWeather,
            minWeather = tomorrowMinWeather,
            condition = tomorrowCondition,
            weatherIcons = iconsMapper.map(tomorrowCondition),
        ),
        WidgetWeatherDay(
            maxWeather = nextTwoDaysMaxWeather,
            minWeather = nextTwoDaysMinWeather,
            condition = nextTwoDaysCondition,
            weatherIcons = iconsMapper.map(nextTwoDaysCondition),
        ),
    ),
)