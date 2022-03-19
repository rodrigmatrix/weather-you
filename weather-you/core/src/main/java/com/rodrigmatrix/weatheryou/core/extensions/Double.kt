package com.rodrigmatrix.weatheryou.core.extensions

fun Double.temperatureString(): String {
    return this.toInt().toString() + "°"
}

fun Double.percentageString(): String {
    return this.toInt().toString() + "%"
}

fun Double.speedString(): String {
    return this.toInt().toString() + "km/h"
}