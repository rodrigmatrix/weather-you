package com.rodrigmatrix.weatheryou.presentation.home

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeViewState(
    val isLoading: Boolean = false,
    val locationsList: List<WeatherLocation> = emptyList()
)