package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class PrecipitationUnitPreference(
    val title: Int,
) {
    IN(R.string.inches),
    MM_CM(R.string.milimeters_centimeters),
}