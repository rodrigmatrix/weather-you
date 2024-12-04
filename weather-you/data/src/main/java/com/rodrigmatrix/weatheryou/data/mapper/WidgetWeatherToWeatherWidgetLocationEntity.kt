package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

fun WeatherLocation.toWeatherWidgetLocationEntity(): WeatherWidgetLocationEntity = WeatherWidgetLocationEntity(
    id = widgetId,
    name = name,
    latitude = latitude,
    longitude = longitude,
    timeZone = timeZone,
    countryCode = countryCode,
    isCurrentLocation = isCurrentLocation,
)