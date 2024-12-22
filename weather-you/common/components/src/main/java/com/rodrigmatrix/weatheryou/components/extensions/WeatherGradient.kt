package com.rodrigmatrix.weatheryou.components.extensions

import androidx.compose.ui.graphics.Color
import com.rodrigmatrix.weatheryou.core.extensions.getDateTimeFromTimezone
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.Duration
import kotlin.math.absoluteValue

fun List<Double>.toGradientList(): List<Color> {
    return this.map {
        when (it) {
            in (-50.0..0.0) -> Color(0xff1a70f3)
            in (0.1..10.0) -> Color(0xff5acaf5)
            in (10.01..13.0) -> Color(0xff8bceb4)
            in (13.01..15.0) -> Color(0xffa1d4a7)
            in (15.01..20.0) -> Color(0xffd2d176)
            in (20.01..25.0) -> Color(0xffcecc75)
            in (25.01..30.0) -> Color(0xfffacc04)
            in (30.01..38.0) -> Color(0xffff820c)
            else -> Color(0xffff820c)
        }
    }
}

fun getGradientList(
    currentTime: DateTime,
    currentCondition: WeatherCondition,
    sunrise: DateTime,
    sunset: DateTime,
    isDaylight: Boolean,
): List<Color> {
    val sunriseDuration = Duration(currentTime, sunrise)
    val sunsetDuration = Duration(currentTime, sunset)
    return when {
        sunriseDuration.standardMinutes.absoluteValue <= 20 -> listOf(
            Color(0xFF203356),
            Color(0xFF715D7C),
            Color(0xFF8D6780),
        )

        sunsetDuration.standardMinutes.absoluteValue <= 15 -> listOf(
            Color(0xFF46679A),
            Color(0xFF7190BF),
            Color(0xFFBCA591),
        )

        currentCondition in listOf(
            WeatherCondition.Fog,
            WeatherCondition.Haze,
            WeatherCondition.Smoke,
        ) -> listOf(
            Color(0xFFBEBFB7),
            Color(0xFFB7BCBA),
            Color(0xFF606D6C),
        )

        currentCondition in listOf(
            WeatherCondition.Cloudy,
            WeatherCondition.Rain,
            WeatherCondition.FreezingRain,
            WeatherCondition.Snow,
            WeatherCondition.Thunderstorm,
            WeatherCondition.Thunderstorms,
            WeatherCondition.SevereThunderstorm,
            WeatherCondition.ScatteredThunderstorms,
            WeatherCondition.MixedRainfall,
            WeatherCondition.MixedRainAndSnow,
            WeatherCondition.HeavyRain,
        ) -> if (isDaylight) {
            listOf(
                Color(0xFF949EA7),
                Color(0xFF646F7D),
                Color(0xFF4F5C69),
            )
        } else {
            listOf(
                Color(0xFF303843),
                Color(0xFF1F2530),
                Color(0xFF252E3F),
            )
        }

        isDaylight -> listOf(
            Color(0xFF2E659D),
            Color(0xFF3E7DB9),
            Color(0xFF66A0DB),
        )

        else -> listOf(
            Color(0xFF0A0F22),
            Color(0xFF262842),
            Color(0xFF374666),
        )
    }
}

fun WeatherLocation.getGradientList(): List<Color> {
    return getGradientList(
        currentTime = timeZone.getDateTimeFromTimezone(),
        currentCondition = currentCondition,
        sunrise = sunrise,
        sunset = sunset,
        isDaylight = isDaylight,
    )
}


fun getTemperatureGradient(
    minDayTemperature: Double,
    maxDayTemperature: Double,
    hours: List<WeatherHour>,
): List<Color> {
    val colors = mutableListOf<Color>()
    listOf(
        minDayTemperature,
        hours.find { it.dateTime.hourOfDay == 12 }?.temperature ?: 0.0,
        maxDayTemperature
    ).forEach {
        val temperatureList = listOf(it)
        if (temperatureList.any { it in (-50.0..0.0) }) {
            colors.add(Color(0xff1a70f3))
        }
        if (temperatureList.any { it in (0.1..10.0) }) {
            colors.add(Color(0xff5acaf5))
        }
        if (temperatureList.any { it in (10.1..15.0) }) {
            colors.add(Color(0xff8bceb4))
        }
        if (temperatureList.any { it in (13.1..15.0) }) {
            colors.add(Color(0xffa1d4a7))
        }
        if (temperatureList.any { it in (15.1..20.0) }) {
            colors.add(Color(0xffd2d176))
        }
        if (temperatureList.any { it in (20.1..25.0) }) {
            colors.add(Color(0xffcecc75))
        }
        if (temperatureList.any { it in (25.1..30.0) }) {
            colors.add(Color(0xfffacc04))
        }
        if (temperatureList.any { it in (30.01..38.0) }) {
            colors.add(Color(0xffff820c))
        }
        if (temperatureList.any { it in (38.01..40.0) }) {
            colors.add(Color(0xfffe412c))
        }
        if (temperatureList.any { it in (50.0..100.0) }) {
            colors.add(Color(0xFF760700))
        }
    }

    return if (colors.size == 1) {
        colors.add(colors[0])
        colors
    } else if (colors.isEmpty()) {
        listOf(Color(0xffa1d4a7), Color(0xffa1d4a7))
    } else {
        colors
    }
}
