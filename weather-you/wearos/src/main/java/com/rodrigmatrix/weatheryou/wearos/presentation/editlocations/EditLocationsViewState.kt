package com.rodrigmatrix.weatheryou.wearos.presentation.editlocations

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class EditLocationsViewState(
    val weatherLocations: List<WeatherLocation> = emptyList(),
    val deletingLocation: WeatherLocation? = null,
) : ViewState