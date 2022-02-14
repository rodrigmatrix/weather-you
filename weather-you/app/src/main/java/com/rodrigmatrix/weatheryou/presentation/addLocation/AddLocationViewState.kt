package com.rodrigmatrix.weatheryou.presentation.addLocation

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.City

data class AddLocationViewState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val locationsList: List<String> = emptyList(),
    val famousLocationsList: List<City> = emptyList()
): ViewState