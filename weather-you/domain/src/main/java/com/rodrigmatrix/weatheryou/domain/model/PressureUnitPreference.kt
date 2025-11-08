package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class PressureUnitPreference(
    val title: Int,
) {
    MBAR(R.string.milibars),
    HPA(R.string.hectopascals),
    INHG(R.string.inches_of_mercury),
    MMHG(R.string.millimeters_of_mercury),
    KPA(R.string.kilopascals),
}