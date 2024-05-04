package com.rodrigmatrix.weatheryou.core.utils

import java.util.Locale

class UnitLocale(
    private val locale: Locale,
) {

    fun getDefault(): Type = when (locale.country) {
        "US", "LR", "MM" -> Type.Imperial
        else -> Type.Metric
    }

    enum class Type {
        Imperial,
        Metric,
    }
}