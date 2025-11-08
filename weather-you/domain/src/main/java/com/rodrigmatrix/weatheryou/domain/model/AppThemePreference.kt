package com.rodrigmatrix.weatheryou.domain.model

import com.rodrigmatrix.weatheryou.domain.R

enum class AppThemePreference(
    val title: Int,
) {
    SYSTEM_DEFAULT(R.string.system_default),
    LIGHT(R.string.light),
    DARK(R.string.dark),
}