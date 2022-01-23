package com.rodrigmatrix.weatheryou.presentation.extensions

fun Double.temperatureString(): String {
    return this.toInt().toString() + "°"
}