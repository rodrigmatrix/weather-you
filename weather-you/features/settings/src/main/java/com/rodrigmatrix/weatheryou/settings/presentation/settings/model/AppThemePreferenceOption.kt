package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode

enum class AppThemePreferenceOption(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val option: ThemeMode,
) {

    SYSTEM_DEFAULT(R.string.system_default, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_nightlight, ThemeMode.System),
    LIGHT(R.string.light, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_light_mode, ThemeMode.Light),
    DARK(R.string.dark, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_dark_mode, ThemeMode.Dark)
}