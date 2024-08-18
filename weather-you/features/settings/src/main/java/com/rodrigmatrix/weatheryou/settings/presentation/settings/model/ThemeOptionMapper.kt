package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference

fun ThemeMode.toPreference() = when (this) {
    ThemeMode.System -> AppThemePreference.SYSTEM_DEFAULT
    ThemeMode.Light -> AppThemePreference.LIGHT
    ThemeMode.Dark -> AppThemePreference.DARK
}
