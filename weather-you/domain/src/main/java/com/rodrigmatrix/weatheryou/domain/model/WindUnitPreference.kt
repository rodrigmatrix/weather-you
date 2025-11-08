package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class WindUnitPreference(
    val title: Int,
) {
    MPH(R.string.miles_per_hour),
    KPH(R.string.kilometers_per_hour),
    MS(R.string.meters_per_second),
    KN(R.string.knots),
}