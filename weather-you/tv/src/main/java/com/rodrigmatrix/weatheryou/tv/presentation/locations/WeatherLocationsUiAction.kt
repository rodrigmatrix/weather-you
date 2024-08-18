package com.rodrigmatrix.weatheryou.tv.presentation.locations

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class WeatherLocationsUiAction : ViewEffect {

    data class Error(@StringRes val stringRes: Int): WeatherLocationsUiAction()
}