package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.visualcrossing.DayResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.HourResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime

private const val DAY_HOURS = 24
private const val SECOND_ELEMENT = 1

class VisualCrossingRemoteMapper(
    private val weatherIconMapper: VisualCrossingWeatherIconMapper
) {

    fun map(source: VisualCrossingWeatherResponse): WeatherLocation {
        return WeatherLocation(
            name = source.resolvedAddress.orEmpty().split(",").dropLast(1).joinToString(),
            latitude = source.latitude ?: 0.0,
            longitude = source.longitude ?: 0.0,
            isCurrentLocation = false,
            currentWeather = source.currentConditions?.temp ?: 0.0,
            currentWeatherDescription = source.currentConditions?.conditions.orEmpty(),
            maxTemperature = source.days?.firstOrNull()?.tempmax ?: 0.0,
            lowestTemperature = source.days?.firstOrNull()?.tempmin ?: 0.0,
            feelsLike = source.currentConditions?.feelslike ?: 0.0,
            weatherIcons = weatherIconMapper.map(source.currentConditions?.icon.orEmpty()),
            currentTime = source.currentConditions?.datetimeEpoch ?: 0L,
            timeZone = source.timezone.orEmpty(),
            precipitationProbability = source.currentConditions?.precipprob ?: 0.0,
            precipitationType = source.currentConditions?.preciptype?.firstOrNull().orEmpty(),
            humidity = source.currentConditions?.humidity ?: 0.0,
            dewPoint = source.currentConditions?.dew ?: 0.0,
            windDirection = source.currentConditions?.winddir ?: 0.0,
            windSpeed = source.currentConditions?.windspeed ?: 0.0,
            uvIndex = source.currentConditions?.uvindex ?: 0.0,
            sunrise = source.currentConditions?.sunriseEpoch ?: 0L,
            sunset = source.currentConditions?.sunsetEpoch ?: 0L,
            visibility = source.currentConditions?.visibility ?: 0.0,
            pressure = source.currentConditions?.pressure ?: 0.0,
            days = source.days?.mapDaysList().orEmpty(),
            hours = source.getTodayHoursList()
        )
    }

    private fun List<DayResponse>.mapDaysList(): List<WeatherDay> {
        return this.map {
            WeatherDay(
                dateTime = it.datetimeEpoch ?: 0L,
                weatherCondition = it.conditions
                    .orEmpty()
                    .split(",")
                    .take(2)
                    .joinToString(),
                temperature = it.temp ?: 0.0,
                maxTemperature = it.tempmax ?: 0.0,
                minTemperature = it.tempmin ?: 0.0,
                weatherIcons = weatherIconMapper.map(it.icon.orEmpty()),
                hours = it.hours?.mapHoursList().orEmpty(),
                precipitationProbability = it.precipprob ?: 0.0,
                precipitationType = it.preciptype?.firstOrNull().orEmpty(),
                windSpeed = it.windspeed ?: 0.0,
                humidity = it.humidity ?: 0.0,
                sunrise = it.sunriseEpoch ?: 0L,
                sunset = it.sunsetEpoch ?: 0L
            )
        }
    }

    private fun List<HourResponse>.mapHoursList(): List<WeatherHour> {
        return this.map {
            WeatherHour(
                dateTime = it.datetimeEpoch ?: 0L,
                weatherCondition = it.conditions.orEmpty(),
                temperature = it.temp ?: 0.0,
                weatherIcons = weatherIconMapper.map(it.icon.orEmpty()),
                precipitationProbability = it.precipprob ?: 0.0,
                precipitationType = it.preciptype?.firstOrNull().orEmpty()
            )
        }
    }

    private fun VisualCrossingWeatherResponse.getTodayHoursList(): List<WeatherHour> {
        val dateZone = DateTimeZone.forID(timezone)
        val currentTime = DateTime(dateZone)

        val todayList = days?.first()?.hours.orEmpty().filter {
            val hourTime = DateTime(dateZone).withTime(LocalTime(it.datetime))
            currentTime.hourOfDay < hourTime.hourOfDay
        }.toMutableList()
        val tomorrowList = days
            ?.get(SECOND_ELEMENT)
            ?.hours
            ?.take(DAY_HOURS - todayList.size)
            .orEmpty()

        return todayList.plus(tomorrowList).mapHoursList()
    }
}