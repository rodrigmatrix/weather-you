package com.rodrigmatrix.weatheryou.presentation.addLocation

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation

data class AddLocationViewState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val locationsList: List<com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation> = emptyList(),
    val famousLocationsList: List<com.rodrigmatrix.weatheryou.domain.model.City> = emptyList()
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState {

    fun isLocationsListVisible(): Boolean {
        return locationsList.isNotEmpty() || searchText.isEmpty() || isLoading
    }
}