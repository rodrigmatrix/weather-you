package com.rodrigmatrix.weatheryou.presentation.home

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeViewState(
    val isLoading: Boolean = false,
    val locationsList: List<WeatherLocation> = emptyList(),
    val error: Throwable? = null,
    val deletePackageDialogVisible: Boolean = false
): ViewState