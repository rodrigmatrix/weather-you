package com.rodrigmatrix.weatheryou.core.extensions

import androidx.compose.runtime.Composable
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import java.lang.Exception
import java.math.RoundingMode
import kotlin.math.roundToInt

@Composable
fun Double.temperatureString(
    temperaturePreference: TemperaturePreference = WeatherYouAppState.appSettings.temperaturePreference,
): String {
    val intTemp = this.roundToInt()
    return when (temperaturePreference) {
        TemperaturePreference.METRIC -> "$intTemp°"
        TemperaturePreference.IMPERIAL -> ((this * 1.8) + 32).roundToInt().toString() + "°"
    }
}

@Composable
fun Double.percentageString(): String {
    return try {
        this.roundToInt().toString() + "%"
    } catch (e: Exception) {
        "${this}%"
    }
}

@Composable
fun Double.speedString(
    temperaturePreference: TemperaturePreference = WeatherYouAppState.appSettings.temperaturePreference,
): String {
    return when (temperaturePreference) {
        TemperaturePreference.METRIC -> this.roundToInt().toString() + " " + "km/h"
        TemperaturePreference.IMPERIAL -> (this * 0.621371).roundToInt().toString() + " " + "mph"
    }
}

@Composable
fun Double.distanceString(
    temperaturePreference: TemperaturePreference = WeatherYouAppState.appSettings.temperaturePreference,
): String {
    return when (temperaturePreference) {
        TemperaturePreference.METRIC -> this.roundToInt().toString() + " " + "km"
        TemperaturePreference.IMPERIAL -> (this * 0.621371).roundToInt().toString() + " " + "mi"
    }
}