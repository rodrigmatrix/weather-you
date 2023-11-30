package com.rodrigmatrix.weatheryou.tv.presentation.search

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation

internal data class SearchLocationUiState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val locationsList: List<SearchAutocompleteLocation> = emptyList(),
    val famousLocationsList: List<City> = emptyList()
) : ViewState {

    fun isLocationsListVisible(): Boolean {
        return locationsList.isNotEmpty() || searchText.isEmpty() || isLoading
    }
}