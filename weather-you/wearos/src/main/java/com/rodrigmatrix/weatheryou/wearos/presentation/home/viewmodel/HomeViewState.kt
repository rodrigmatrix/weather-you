package com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeViewState(
    val isLoading: Boolean = false,
    val weatherLocations: List<WeatherLocation> = emptyList(),
    val pages: List<HomePage> = emptyList(),
    @StringRes val error: Int? = null
): ViewState

sealed interface HomePage {

    object Settings : HomePage

    object EmptyState : HomePage

    data class Weather(val weatherLocation: WeatherLocation) : HomePage
}