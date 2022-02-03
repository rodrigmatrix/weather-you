package com.rodrigmatrix.weatheryou.presentation.addLocation

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState

data class AddLocationViewState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val locationsList: List<String> = emptyList()
): ViewState