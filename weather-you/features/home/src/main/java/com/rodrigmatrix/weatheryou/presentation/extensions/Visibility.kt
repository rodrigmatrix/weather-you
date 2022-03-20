package com.rodrigmatrix.weatheryou.presentation.extensions

import com.rodrigmatrix.weatheryou.home.R

fun Double.visibilityConditionsStringRes(): Int {
    return when(this) {
        in 0.0..0.049 -> R.string.dense_fog
        in 0.050..0.500 -> R.string.thick_fog
        in 0.200..0.500 -> R.string.moderate_fog
        in 0.700..1.0 -> R.string.light_fog
        in 2.0..2.799 -> R.string.thin_fog
        in 2.8..4.0 -> R.string.haze
        in 4.0..9.999 -> R.string.light_haze
        else -> R.string.clear_visibility
    }
}