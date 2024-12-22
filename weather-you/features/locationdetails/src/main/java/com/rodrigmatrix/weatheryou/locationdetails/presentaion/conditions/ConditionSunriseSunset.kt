package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.runtime.Composable
import com.rodrigmatrix.weatheryou.components.details.SunriseSunsetCardContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
private fun ConditionSunriseSunset(
    weatherLocation: WeatherLocation,
    day: WeatherDay,
) {
    SunriseSunsetCardContent(
        sunrise = weatherLocation.sunrise,
        sunset = weatherLocation.sunset,
        currentTime = day.dateTime,
        isDaylight = if (weatherLocation.days.indexOf(day) == 0) {
            weatherLocation.isDaylight
        } else {
            false
        },
    )
}