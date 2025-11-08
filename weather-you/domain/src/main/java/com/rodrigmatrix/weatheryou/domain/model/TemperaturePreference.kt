package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class TemperaturePreference(
    val title: Int,
) {
    METRIC(R.string.metric_preference),
    IMPERIAL(R.string.imperial_preference),
    KELVIN(R.string.kelvin),
}