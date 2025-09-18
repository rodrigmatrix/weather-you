package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode

enum class AppThemePreferenceOption(
    @field:StringRes val title: Int,
    @field:DrawableRes val icon: Int,
    val option: AppThemePreference,
) {

    SYSTEM_DEFAULT(R.string.system_default, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_nightlight, AppThemePreference.SYSTEM_DEFAULT),
    LIGHT(R.string.light, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_light_mode, AppThemePreference.LIGHT),
    DARK(R.string.dark, com.rodrigmatrix.weatheryou.settings.R.drawable.ic_dark_mode, AppThemePreference.DARK)
}