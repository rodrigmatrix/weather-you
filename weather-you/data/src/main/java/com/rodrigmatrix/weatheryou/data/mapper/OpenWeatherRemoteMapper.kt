package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherDaily
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherHourly
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherLocationResponse
import com.rodrigmatrix.weatheryou.domain.model.MoonPhase
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class OpenWeatherRemoteMapper(
    private val weatherConditionMapper: OpenWeatherConditionMapper
) {

    fun map(source: OpenWeatherLocationResponse): WeatherLocation {
        val daysList = source.daily?.mapDaysList(source.timezone).orEmpty()
        return WeatherLocation(
            id = 0,
            widgetId = "",
            orderIndex = 0,
            name = source.name.orEmpty().split(",").dropLast(1).joinToString(),
            latitude = source.lat ?: 0.0,
            longitude = source.lon ?: 0.0,
            isCurrentLocation = false,
            currentWeather = source.current?.temp ?: 0.0,
            currentCondition = weatherConditionMapper.map(source.current?.weather?.first()?.description),
            maxTemperature = source.daily?.firstOrNull()?.temp?.max ?: 0.0,
            lowestTemperature = source.daily?.firstOrNull()?.temp?.min ?: 0.0,
            feelsLike = source.current?.feelsLike ?: 0.0,
            currentTime = source.current?.datetime.toDateTime(source.timezone),
            timeZone = source.timezone.orEmpty(),
            precipitationProbability = source.hourly.toChanceOfPrecipitation(),
            precipitationType = PrecipitationType.Clear,
            humidity = source.current?.humidity ?: 0.0,
            dewPoint = source.current?.dewPoint ?: 0.0,
            windDirection = source.current?.windDeg ?: 0.0,
            windSpeed = source.current?.windSpeed ?: 0.0,
            uvIndex = source.current?.uvi ?: 0.0,
            sunrise = source.current?.sunrise.toDateTime(source.timezone),
            sunset = source.current?.sunset.toDateTime(source.timezone),
            visibility = (source.current?.visibility ?: 0.0) / 1000.0,
            pressure = source.current?.pressure?.toDouble() ?: 0.0,
            days = daysList,
            hours = source.getTodayHoursList(source.timezone),
            expirationDate = source.current?.datetime.toDateTime(source.timezone),
            minWeekTemperature = daysList.minOf { it.minTemperature },
            maxWeekTemperature = daysList.maxOf { it.maxTemperature },
            cloudCover = 0.0,
            countryCode = "",
        )
    }

    private fun List<OpenWeatherDaily>.mapDaysList(
        timeZone: String?,
    ): List<WeatherDay> {
        return this.map {
            WeatherDay(
                dateTime = it.datetime.toDateTime(timeZone),
                weatherCondition = weatherConditionMapper.map(it.weather?.firstOrNull()?.main),
                temperature = it.temp?.day ?: 0.0,
                maxTemperature = it.temp?.max ?: 0.0,
                minTemperature = it.temp?.min ?: 0.0,
                precipitationProbability = it.pop.calculatePrecipitation(),
                precipitationType = PrecipitationType.Clear,
                windSpeed = it.windSpeed ?: 0.0,
                humidity = it.humidity ?: 0.0,
                sunrise = it.sunrise.toDateTime(timeZone),
                sunset = it.sunset.toDateTime(timeZone),
                hours = emptyList(),
                precipitationAmount = 0.0,
                snowfallAmount = 0.0,
                solarNoon = DateTime(),
                solarMidnight = DateTime(),
                moonPhase = MoonPhase.New,
                sunriseAstronomical = DateTime(),
                sunsetAstronomical = DateTime(),
                sunriseNautical = DateTime(),
                sunsetNautical = DateTime(),
                sunriseCivil = DateTime(),
                sunsetCivil = DateTime(),
            )
        }
    }

    private fun List<OpenWeatherHourly>.mapHoursList(timeZone: String?): List<WeatherHour> {
        return this.map {
            WeatherHour(
                dateTime = it.datetime.toDateTime(timeZone),
                weatherCondition = weatherConditionMapper.map(it.weather?.first()?.description),
                temperature = it.temp ?: 0.0,
                precipitationProbability = it.pop.calculatePrecipitation(),
                precipitationType = PrecipitationType.Clear,
                cloudCover = it.clouds?.toDouble() ?: 0.0,
                feelsLike = it.feelsLike ?: 0.0,
                humidity = it.humidity ?: 0.0,
                visibility = it.visibility?.toDouble() ?: 0.0,
                windSpeed = it.windSpeed ?: 0.0,
                windDirection = it.windDeg ?: 0,
                uvIndex = it.uvi ?: 0.0,
                snowfallIntensity = 0.0,
                precipitationAmount = 0.0,
            )
        }
    }

    private fun OpenWeatherLocationResponse.getTodayHoursList(timeZone: String?): List<WeatherHour> {
        return hourly?.mapHoursList(timeZone).orEmpty()
    }

    private fun List<OpenWeatherHourly>?.toChanceOfPrecipitation(): Double {
        return this?.firstOrNull()?.pop.calculatePrecipitation()
    }

    private fun Double?.calculatePrecipitation(): Double = (this ?: 0.0) * 100.0

    private fun Long?.toDateTime(timeZone: String?): DateTime {
        return DateTime(this ?: 0L, DateTimeZone.forID(timeZone))
    }
}