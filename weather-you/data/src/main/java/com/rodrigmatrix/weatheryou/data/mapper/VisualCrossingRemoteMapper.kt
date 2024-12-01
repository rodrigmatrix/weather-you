package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.visualcrossing.DayResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.HourResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime

private const val DAY_HOURS = 24
private const val SECOND_ELEMENT = 1

class VisualCrossingRemoteMapper(
    private val weatherConditionMapper: VisualCrossingWeatherConditionMapper
) {

    fun map(source: VisualCrossingWeatherResponse): WeatherLocation {
        val currentTime = source.currentConditions?.datetimeEpoch.toDateTime(source.timezone)
        val sunrise = source.currentConditions?.sunriseEpoch.toDateTime(source.timezone)
        val sunset = source.currentConditions?.sunriseEpoch.toDateTime(source.timezone)
        val sunriseSunsetRange = sunrise.hourOfDay..sunset.hourOfDay
        return WeatherLocation(
            id = 0,
            name = if (source.resolvedAddress.orEmpty().hasLetters()) {
                source.resolvedAddress.orEmpty().split(",").dropLast(1).joinToString()
            } else { "" },
            latitude = source.latitude ?: 0.0,
            longitude = source.longitude ?: 0.0,
            isCurrentLocation = false,
            currentWeather = source.currentConditions?.temp ?: 0.0,
            currentCondition = weatherConditionMapper.map(source.currentConditions?.icon),
            maxTemperature = source.days?.firstOrNull()?.tempmax ?: 0.0,
            lowestTemperature = source.days?.firstOrNull()?.tempmin ?: 0.0,
            feelsLike = source.currentConditions?.feelslike ?: 0.0,
            currentTime = currentTime,
            timeZone = source.timezone.orEmpty(),
            isDaylight = currentTime.hourOfDay in sunriseSunsetRange,
            precipitationProbability = source.currentConditions?.precipprob ?: 0.0,
            precipitationType = source.currentConditions?.preciptype?.firstOrNull().orEmpty(),
            humidity = source.currentConditions?.humidity ?: 0.0,
            dewPoint = source.currentConditions?.dew ?: 0.0,
            windDirection = source.currentConditions?.winddir ?: 0.0,
            windSpeed = source.currentConditions?.windspeed ?: 0.0,
            uvIndex = source.currentConditions?.uvindex ?: 0.0,
            sunrise = sunrise,
            sunset = sunset,
            visibility = source.currentConditions?.visibility ?: 0.0,
            pressure = source.currentConditions?.pressure ?: 0.0,
            days = source.days?.mapDaysList(source.timezone).orEmpty(),
            hours = source.getTodayHoursList(),
            expirationDate = currentTime,
            minWeekTemperature = source.days?.minOf { it.tempmin ?: 0.0 } ?: 0.0,
            maxWeekTemperature = source.days?.maxOf { it.tempmax ?: 0.0 } ?: 0.0,
            cloudCover = 0.0,
        )
    }

    private fun List<DayResponse>.mapDaysList(
        timeZone: String?,
    ): List<WeatherDay> {
        return this.map {
            WeatherDay(
                dateTime = it.datetimeEpoch.toDateTime(timeZone),
                weatherCondition = weatherConditionMapper.map(it.icon),
                temperature = it.temp ?: 0.0,
                maxTemperature = it.tempmax ?: 0.0,
                minTemperature = it.tempmin ?: 0.0,
                hours = it.hours?.mapHoursList(timeZone).orEmpty(),
                precipitationProbability = it.precipprob ?: 0.0,
                precipitationType = it.preciptype?.firstOrNull().orEmpty(),
                windSpeed = it.windspeed ?: 0.0,
                humidity = it.humidity ?: 0.0,
                sunrise = it.sunriseEpoch.toDateTime(timeZone),
                sunset = it.sunsetEpoch.toDateTime(timeZone),
            )
        }
    }

    private fun String.hasLetters() = any { it.isLetter() }

    private fun List<HourResponse>.mapHoursList(timeZone: String?): List<WeatherHour> {
        return this.map {
            WeatherHour(
                dateTime = it.datetimeEpoch.toDateTime(timeZone),
                weatherCondition = weatherConditionMapper.map(it.icon),
                temperature = it.temp ?: 0.0,
                precipitationProbability = it.precipprob ?: 0.0,
                precipitationType = it.preciptype?.firstOrNull().orEmpty(),
                cloudCover = it.cloudcover ?: 0.0,
                feelsLike = it.feelslike ?: 0.0,
                humidity = it.humidity ?: 0.0,
                visibility = it.visibility ?: 0.0,
                windSpeed = it.windspeed ?: 0.0,
                windDirection = it.winddir?.toInt() ?: 0,
                uvIndex = it.uvindex ?: 0.0,
                snowfallIntensity = it.snow ?: 0.0,
                precipitationAmount = it.precip ?: 0.0,
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

        return todayList.plus(tomorrowList).mapHoursList(timezone)
    }

    private fun Long?.toDateTime(timeZone: String?): DateTime {
        return DateTime(this?.toUnixTimestamp() ?: 0L, DateTimeZone.forID(timeZone))
    }

    private fun Long.toUnixTimestamp(): Long = this * 1000L
}