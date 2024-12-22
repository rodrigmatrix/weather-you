package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.weatherkit.Day
import com.rodrigmatrix.weatheryou.data.model.weatherkit.Hour
import com.rodrigmatrix.weatheryou.data.model.weatherkit.WeatherKitLocationResponse
import com.rodrigmatrix.weatheryou.domain.model.MoonPhase
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.math.RoundingMode
import java.text.DecimalFormat

class WeatherKitRemoteMapper(
    private val weatherKitConditionMapper: WeatherKitConditionMapper
) {

    fun map(
        source: WeatherKitLocationResponse,
        latitude: Double,
        longitude: Double,
        timezone: String,
        countryCode: String,
    ): WeatherLocation {
        val daysList = source.forecastDaily?.days?.mapDaysList(
            source.forecastHourly?.hours,
            timezone,
        ).orEmpty()
        return WeatherLocation(
            id = 0,
            name = "",
            countryCode = countryCode,
            latitude = latitude,
            longitude = longitude,
            isCurrentLocation = false,
            currentWeather = source.currentWeather?.temperature.round(),
            feelsLike = source.currentWeather?.temperatureApparent.round(),
            maxTemperature = source.forecastDaily?.days?.first()?.temperatureMax.round(),
            lowestTemperature = source.forecastDaily?.days?.first()?.temperatureMin.round(),
            currentCondition = weatherKitConditionMapper.map(
                source = source.currentWeather?.conditionCode
            ),
            currentTime = timezone.getCurrentTime(),
            timeZone = timezone,
            precipitationProbability = source.forecastDaily?.days?.first()?.precipitationChance.toPercentage(),
            precipitationType = source.forecastDaily?.days?.first()?.precipitationType.toPrecipitationType(),
            isDaylight = source.currentWeather?.daylight == true,
            humidity = source.currentWeather?.humidity.toPercentage(),
            dewPoint = source.currentWeather?.temperatureDewPoint ?: 0.0,
            windSpeed = source.currentWeather?.windSpeed ?: 0.0,
            windDirection = source.currentWeather?.windDirection?.toDouble() ?: 0.0,
            uvIndex = source.currentWeather?.uvIndex?.toDouble() ?: 0.0,
            sunrise = source.forecastDaily?.days?.first()?.sunrise.toDateTime(timezone),
            sunset = source.forecastDaily?.days?.first()?.sunset.toDateTime(timezone),
            visibility = ((source.currentWeather?.visibility ?: 1000.0) / 1000),
            pressure = source.currentWeather?.pressure ?: 0.0,
            days = daysList,
            hours = source.forecastHourly?.hours?.getTodayForecast(timezone).orEmpty(),
            expirationDate = source.currentWeather?.metadata?.expireTime.toDateTime(timezone),
            maxWeekTemperature = daysList.maxOf { it.maxTemperature },
            minWeekTemperature = daysList.minOf { it.minTemperature },
            cloudCover = source.currentWeather?.cloudCover.toPercentage(),
        )
    }

    private fun List<Day>.mapDaysList(
        hours: List<Hour>?,
        timezone: String,
    ): List<WeatherDay> {
        return this.map {
            WeatherDay(
                dateTime = it.forecastStart.toDateTime(timezone),
                weatherCondition = weatherKitConditionMapper.map(source = it.conditionCode),
                temperature = it.temperatureMax.round(),
                maxTemperature = it.temperatureMax.round(),
                minTemperature = it.temperatureMin.round(),
                hours = hours?.mapHoursList(it.forecastStart, timezone).orEmpty(),
                precipitationProbability = it.precipitationChance.toPercentage(),
                precipitationType = it.precipitationType.toPrecipitationType(),
                windSpeed = it.windSpeedAvg ?: 0.0,
                humidity = hours.orEmpty().maxOf { it.humidity.toPercentage() },
                sunrise = it.sunrise.toDateTime(timezone),
                sunset = it.sunrise.toDateTime(timezone),
                precipitationAmount = it.precipitationAmount ?: 0.0,
                snowfallAmount = it.snowfallAmount ?: 0.0,
                solarNoon = it.solarNoon.toDateTime(timezone),
                solarMidnight = it.solarMidnight.toDateTime(timezone),
                moonPhase = try {
                    MoonPhase.valueOf(it.moonPhase.orEmpty())
                } catch (_: Exception) {
                    MoonPhase.New
                },
                sunriseAstronomical = it.sunriseAstronomical.toDateTime(timezone),
                sunsetAstronomical = it.sunsetAstronomical.toDateTime(timezone),
                sunriseNautical = it.sunriseNautical.toDateTime(timezone),
                sunsetNautical = it.sunsetNautical.toDateTime(timezone),
                sunriseCivil = it.sunriseCivil.toDateTime(timezone),
                sunsetCivil = it.sunsetCivil.toDateTime(timezone),
            )
        }
    }

    private fun List<Hour>.getTodayForecast(timezone: String): List<WeatherHour> {
        val dateTimeNow = timezone.getCurrentTime().minusHours(1)
        val tomorrowDateTime = dateTimeNow.plusHours(24)

        return this.filter {
            val forecastStart = it.forecastStart.toDateTime(timezone)
            forecastStart.isAfter(dateTimeNow) &&
                    forecastStart.isBefore(tomorrowDateTime)
        }.map {
            WeatherHour(
                dateTime = it.forecastStart.toDateTime(timezone),
                weatherCondition = weatherKitConditionMapper.map(it.conditionCode),
                temperature = it.temperature.round(),
                isDaylight = it.daylight == true,
                precipitationProbability = it.precipitationChance.toPercentage(),
                precipitationType = it.precipitationType.toPrecipitationType(),
                precipitationAmount = it.precipitationAmount ?: 0.0,
                cloudCover = it.cloudCover.toPercentage(),
                feelsLike = it.temperatureApparent ?: 0.0,
                humidity = it.humidity.toPercentage(),
                visibility = it.visibility ?: 0.0,
                windSpeed = it.windSpeed ?: 0.0,
                windDirection = it.windDirection ?: 0,
                uvIndex = it.uvIndex?.toDouble() ?: 0.0,
                snowfallIntensity = it.snowfallIntensity ?: 0.0,
            )
        }
    }

    private fun List<Hour>.mapHoursList(date: String?, timezone: String): List<WeatherHour> {
        var plusOne = false
        return this.filter {
            if (date.toDateTime(timezone).isSameDay(it.forecastStart.toDateTime(timezone))) {
                plusOne = true
                true
            } else if (plusOne) {
                plusOne = false
                true
            } else {
                false
            }
        }.map {
            WeatherHour(
                dateTime = it.forecastStart.toDateTime(timezone),
                weatherCondition = weatherKitConditionMapper.map(it.conditionCode),
                isDaylight = it.daylight == true,
                temperature = it.temperature.round(),
                precipitationProbability = it.precipitationChance.toPercentage(),
                precipitationType = it.precipitationType.toPrecipitationType(),
                precipitationAmount = it.precipitationAmount ?: 0.0,
                cloudCover = it.cloudCover.toPercentage(),
                feelsLike = it.temperatureApparent ?: 0.0,
                humidity = it.humidity.toPercentage(),
                visibility = it.visibility ?: 0.0,
                windSpeed = it.windSpeed ?: 0.0,
                windDirection = it.windDirection ?: 0,
                uvIndex = it.uvIndex?.toDouble() ?: 0.0,
                snowfallIntensity = it.snowfallIntensity ?: 0.0,
            )
        }
    }

    private fun DateTime.isSameDay(dateTime: DateTime): Boolean {
        return this.year == dateTime.year &&
                this.monthOfYear == dateTime.monthOfYear &&
                this.dayOfMonth == dateTime.dayOfMonth
    }

    private fun Double?.round(): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(this ?: 0.0).toDouble()
    }

    private fun String?.toDateTime(timezone: String): DateTime {
        return try {
            if (timezone.isEmpty()) {
                DateTime.parse(this)
            } else {
                DateTime.parse(this).toDateTime(DateTimeZone.forID(timezone))
            }
        } catch (_: Exception) {
            DateTime.now()
        }
    }

    private fun Double?.toPercentage(): Double {
        return this?.times(100.0) ?: 0.0
    }
}