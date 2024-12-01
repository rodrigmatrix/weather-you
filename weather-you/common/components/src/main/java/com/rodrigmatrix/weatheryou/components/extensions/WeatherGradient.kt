package com.rodrigmatrix.weatheryou.components.extensions

import androidx.compose.ui.graphics.Color
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import java.util.TimeZone
import kotlin.math.absoluteValue


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
        sunriseDuration.standardMinutes.absoluteValue <= 30 -> listOf(
            Color(0xFF203356),
            Color(0xFF715D7C),
            Color(0xFF8D6780),
        )

        sunsetDuration.standardMinutes.absoluteValue <= 30 -> listOf(
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
        currentTime = currentTime,
        currentCondition = currentCondition,
        sunrise = sunrise,
        sunset = sunset,
        isDaylight = isDaylight,
    )
}