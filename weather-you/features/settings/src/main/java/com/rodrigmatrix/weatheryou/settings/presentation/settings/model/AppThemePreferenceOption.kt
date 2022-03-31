package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.settings.R

enum class AppThemePreferenceOption(
    @StringRes val title: Int,
    val option: AppThemePreference
) {

    SYSTEM_DEFAULT(R.string.system_default, AppThemePreference.SYSTEM_DEFAULT),
    LIGHT(R.string.light, AppThemePreference.LIGHT),
    DARK(R.string.dark, AppThemePreference.DARK)
}