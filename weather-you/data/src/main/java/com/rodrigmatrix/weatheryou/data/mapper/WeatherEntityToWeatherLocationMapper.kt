package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherDayEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherHourEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

fun WeatherEntity.toWeatherLocation(
    id: Int,
    widgetId: String = "",
    orderIndex: Int,
) = WeatherLocation(
    id = id,
    widgetId = widgetId,
    orderIndex = orderIndex,
    name = name,
    latitude = latitude,
    longitude = longitude,
    currentWeather = currentWeather,
    currentCondition = WeatherCondition.valueOf(currentCondition),
    maxTemperature = maxTemperature,
    lowestTemperature = lowestTemperature,
    feelsLike = feelsLike,
    currentTime = currentTime.toDateTime(),
    isCurrentLocation = isCurrentLocation,
    isDaylight = isDaylight,
    expirationDate = expirationDate.toDateTime(),
    timeZone = timeZone,
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    humidity = humidity,
    dewPoint = dewPoint,
    windSpeed = windSpeed,
    windDirection = windDirection,
    uvIndex = uvIndex,
    sunrise = sunrise.toDateTime(),
    sunset = sunset.toDateTime(),
    visibility = visibility,
    pressure = pressure,
    days = days.map { it.toWeatherDay(timeZone) },
    hours = hours.map { it.toWeatherHour(timeZone) },
    countryCode = countryCode,
    minWeekTemperature = minWeekTemperature,
    maxWeekTemperature = maxWeekTemperature,
    cloudCover = cloudCover,
)

fun WeatherDayEntity.toWeatherDay(timeZone: String) = WeatherDay(
    dateTime = dateTime.toDateTime(timeZone),
    maxTemperature = maxTemperature,
    minTemperature = minTemperature,
    weatherCondition = WeatherCondition.valueOf(weatherCondition),
    hours = hours.map { it.toWeatherHour(timeZone) },
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    windSpeed = windSpeed,
    humidity = humidity,
    sunrise = sunrise.toDateTime(),
    sunset = sunset.toDateTime(),
    temperature = temperature,
)

fun WeatherHourEntity.toWeatherHour(timeZone: String) = WeatherHour(
    temperature = temperature,
    weatherCondition = WeatherCondition.valueOf(weatherCondition),
    isDaylight = isDaylight,
    dateTime = dateTime.toDateTime(),
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    feelsLike = feelsLike,
    cloudCover = cloudCover,
    humidity = humidity,
    visibility = visibility,
    precipitationAmount = precipitationAmount,
    windSpeed = windSpeed,
    windDirection = windDirection,
    uvIndex = uvIndex,
    snowfallIntensity = snowfallIntensity,
)


fun WeatherLocation.toWeatherEntity() = WeatherEntity(
    name = name,
    latitude = latitude,
    longitude = longitude,
    currentWeather = currentWeather,
    currentCondition = currentCondition.name,
    maxTemperature = maxTemperature,
    lowestTemperature = lowestTemperature,
    feelsLike = feelsLike,
    currentTime = currentTime.toEntityString(),
    isCurrentLocation = isCurrentLocation,
    isDaylight = isDaylight,
    expirationDate = expirationDate.toEntityString(),
    timeZone = timeZone,
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    humidity = humidity,
    dewPoint = dewPoint,
    windSpeed = windSpeed,
    windDirection = windDirection,
    uvIndex = uvIndex,
    sunrise = sunrise.toEntityString(),
    sunset = sunset.toEntityString(),
    visibility = visibility,
    pressure = pressure,
    days = days.map { it.toWeatherDay() },
    hours = hours.map { it.toWeatherHour() },
    countryCode = countryCode,
    minWeekTemperature = minWeekTemperature,
    maxWeekTemperature = maxWeekTemperature,
    cloudCover = cloudCover,
)

fun WeatherLocation.toWeatherLocationEntity() = WeatherLocationEntity(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    timeZone = timeZone,
    orderIndex = orderIndex,
    countryCode = countryCode,
)

fun WeatherDay.toWeatherDay() = WeatherDayEntity(
    dateTime = dateTime.toEntityString(),
    maxTemperature = maxTemperature,
    minTemperature = minTemperature,
    weatherCondition = weatherCondition.name,
    hours = hours.map { it.toWeatherHour() },
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    windSpeed = windSpeed,
    humidity = humidity,
    sunrise = sunrise.toEntityString(),
    sunset = sunset.toEntityString(),
    temperature = temperature,
)

fun WeatherHour.toWeatherHour() = WeatherHourEntity(
    temperature = temperature,
    weatherCondition = weatherCondition.name,
    isDaylight = isDaylight,
    dateTime = dateTime.toEntityString(),
    precipitationProbability = precipitationProbability,
    precipitationType = precipitationType,
    feelsLike = feelsLike,
    cloudCover = cloudCover,
    humidity = humidity,
    visibility = visibility,
    precipitationAmount = precipitationAmount,
    windSpeed = windSpeed,
    windDirection = windDirection,
    uvIndex = uvIndex,
    snowfallIntensity = snowfallIntensity,
)