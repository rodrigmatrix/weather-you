package com.rodrigmatrix.weatheryou.wearos.presentation.extension

import com.rodrigmatrix.weatheryou.wearos.R

fun Int.uvIndexStringRes(): Int {
    return when (this) {
        in 0..2 -> R.string.low
        in 3..5 -> R.string.moderate
        in 6..7 -> R.string.high
        in 8..10 -> R.string.very_high
        else -> R.string.low
    }
}