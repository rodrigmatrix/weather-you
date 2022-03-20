package com.rodrigmatrix.weatheryou.presentation.extensions

import com.rodrigmatrix.weatheryou.home.R

fun Int.uvIndexStringRes(): Int {
    return when (this) {
        in 0..2 -> R.string.low
        in 3..5 -> R.string.moderate
        in 6..7 -> R.string.high
        in 8..10 -> R.string.very_high
        else -> R.string.low
    }
}

fun Int.uvIndexAlertStringRes(): Int {
    return when (this) {
        in 0..2 -> R.string.low_uv_alert
        in 3..5 -> R.string.moderate_uv_alert
        in 6..7 -> R.string.high_uv_alert
        in 8..10 -> R.string.very_high_uv_alert
        else -> R.string.low_uv_alert
    }
}