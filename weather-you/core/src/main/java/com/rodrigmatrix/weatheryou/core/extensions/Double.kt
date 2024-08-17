package com.rodrigmatrix.weatheryou.core.extensions

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import java.lang.Exception
import kotlin.math.roundToInt

fun Double.temperatureString(): String {
    return try {
        this.roundToInt().toString() + "°"
    } catch (e: Exception) {
        "${this}°"
    }
}

fun Double.percentageString(): String {
    return try {
        this.roundToInt().toString() + "%"
    } catch (e: Exception) {
        "${this}%"
    }
}

fun Double.speedString(unit: TemperaturePreference): String {
    return try {
        val unitString = when (unit) {
            TemperaturePreference.METRIC -> "km/h"
            TemperaturePreference.IMPERIAL -> "mph"
        }
        this.roundToInt().toString() + " " + unitString
    } catch (e: Exception) {
        ""
    }
}