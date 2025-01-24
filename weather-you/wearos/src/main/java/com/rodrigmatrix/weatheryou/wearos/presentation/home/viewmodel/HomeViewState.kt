package com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeViewState(
    val isLoading: Boolean = false,
    val weatherLocations: List<WeatherLocation> = emptyList(),
    @StringRes val error: Int? = null
): ViewState