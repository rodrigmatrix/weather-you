package com.rodrigmatrix.weatheryou.core.extensions

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

fun Double.speedString(): String {
    return try {
        this.roundToInt().toString() + "km/h"
    } catch (e: Exception) {
        "${this}km/h"
    }
}