package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.DayResponse
import com.rodrigmatrix.weatheryou.data.model.HourResponse
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.domain.model.Day
import com.rodrigmatrix.weatheryou.domain.model.Hour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.extensions.getCurrentTime
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime
import java.io.File.separator

private const val DAY_HOURS = 24
private const val SECOND_ELEMENT = 1

class VisualCrossingRemoteMapper(
    private val weatherIconMapper: WeatherIconMapper
) {

    fun map(source: VisualCrossingWeatherResponse): WeatherLocation {
        return WeatherLocation(
            name = source.resolvedAddress.orEmpty().split(",").dropLast(1).joinToString(),
            currentWeather = source.currentConditions?.temp ?: 0.0,
            currentWeatherDescription = source.currentConditions?.conditions.orEmpty(),
            maxTemperature = source.days?.firstOrNull()?.tempmax ?: 0.0,
            lowestTemperature = source.days?.firstOrNull()?.tempmin ?: 0.0,
            feelsLike = source.currentConditions?.feelslike ?: 0.0,
            currentWeatherIcon = weatherIconMapper.map(source.currentConditions?.icon.orEmpty()),
            currentTime = source.timezone?.getCurrentTime().orEmpty(),
            days = source.days?.mapDaysList().orEmpty(),
            hours = source.getTodayHoursList(),
            timeZone = source.timezone.orEmpty()
        )
    }

    private fun List<DayResponse>.mapDaysList(): List<Day> {
        return this.map {
            Day(
                dateTime = it.datetime.orEmpty(),
                weatherCondition = it.conditions.orEmpty(),
                temperature = it.temp ?: 0.0,
                maxTemperature = it.tempmax ?: 0.0,
                minTemperature = it.tempmin ?: 0.0,
                icon = weatherIconMapper.map(it.icon.orEmpty()),
                hours = it.hours?.mapHoursList().orEmpty()
            )
        }
    }

    private fun List<HourResponse>.mapHoursList(): List<Hour> {
        return this.map {
            Hour(
                dateTime = it.datetime.orEmpty(),
                weatherCondition = it.conditions.orEmpty(),
                temperature = it.temp ?: 0.0,
                icon = weatherIconMapper.map(it.icon.orEmpty())
            )
        }
    }

    private fun VisualCrossingWeatherResponse.getTodayHoursList(): List<Hour> {
        val dateZone = DateTimeZone.forID(timezone)
        val currentTime = DateTime(dateZone)

        val todayList = days?.first()?.hours.orEmpty().filter {
            val hourTime = DateTime(dateZone).withTime(LocalTime(it.datetime))
            currentTime.isBefore(hourTime)
        }.toMutableList()
        val tomorrowList = days
            ?.get(SECOND_ELEMENT)
            ?.hours
            ?.take(DAY_HOURS - todayList.size)
            .orEmpty()

        return todayList.plus(tomorrowList).mapHoursList()
    }
}