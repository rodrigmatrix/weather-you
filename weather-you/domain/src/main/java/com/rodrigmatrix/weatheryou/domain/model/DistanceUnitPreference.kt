package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class DistanceUnitPreference(
    val title: Int,
) {
    MI(R.string.miles),
    KM(R.string.kilometers),
}