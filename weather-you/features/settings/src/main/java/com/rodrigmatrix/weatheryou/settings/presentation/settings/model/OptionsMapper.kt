package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference

fun TemperaturePreference.toOption(): TemperaturePreferenceOption {
    return TemperaturePreferenceOption.entries.find {
        it.option == this
    } ?: TemperaturePreferenceOption.METRIC
}

fun AppThemePreference.toOption(): AppThemePreferenceOption {
    return AppThemePreferenceOption.entries.find {
        it.option.toPreference() == this
    } ?: AppThemePreferenceOption.SYSTEM_DEFAULT
}

fun AppColorPreference.toOption(): AppColorPreferenceOption {
    return AppColorPreferenceOption.entries.find {
        it.option.toPreference() == this
    } ?: AppColorPreferenceOption.DEFAULT
}