package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather

fun WidgetWeather.toWeatherWidgetLocationEntity(): WeatherWidgetLocationEntity = WeatherWidgetLocationEntity(
    id = id,
    name = name,
    latitude = lat,
    longitude = long,
    currentWeather = currentWeather,
    currentCondition = currentCondition.name,
    maxWeather = maxWeather,
    minWeather = minWeather,
    lastUpdate = lastUpdate,
    nextHourWeather = hours[0].weather,
    nextHourCondition = hours[0].condition.name,
    nextTwoHoursWeather = hours[1].weather,
    nextTwoHoursCondition = hours[1].condition.name,
    nextThreeHoursWeather = hours[2].weather,
    nextThreeHoursCondition = hours[2].condition.name,
    nextFourHoursWeather = hours[3].weather,
    nextFourHoursCondition = hours[3].condition.name,
    tomorrowMaxWeather = days[0].maxWeather,
    tomorrowMinWeather = days[0].minWeather,
    tomorrowCondition = days[0].condition.name,
    nextTwoDaysMaxWeather = days[1].maxWeather,
    nextTwoDaysMinWeather = days[1].minWeather,
    nextTwoDaysCondition = days[1].condition.name,
)