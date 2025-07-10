package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.R

enum class TemperaturePreferenceOption(
    @StringRes val title: Int,
    val option: TemperaturePreference
) {

    METRIC(R.string.metric_preference, TemperaturePreference.METRIC),
    IMPERIAL(R.string.imperial_preference, TemperaturePreference.IMPERIAL)
}