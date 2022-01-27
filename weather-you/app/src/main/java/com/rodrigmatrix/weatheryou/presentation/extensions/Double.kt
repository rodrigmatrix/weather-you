package com.rodrigmatrix.weatheryou.presentation.extensions

fun Double.temperatureString(): String {
    return this.toInt().toString() + "°"
}

fun Double.percentageString(): String {
    return this.toInt().toString() + "%"
}

fun Double.windSpeed(): String {
    return this.toInt().toString() + "km/h"
}