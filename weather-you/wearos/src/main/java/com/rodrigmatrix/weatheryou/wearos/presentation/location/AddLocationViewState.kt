package com.rodrigmatrix.weatheryou.wearos.presentation.location

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation

data class AddLocationViewState(
    val searchText: String = "",
    val searchHelperText: String = "",
    val isLoadingLocations: Boolean = false,
    val isAddingLocation: Boolean = false,
    val famousLocations: List<City> = emptyList(),
    val searchedLocations: List<SearchAutocompleteLocation> = emptyList(),
    val showEmptyState: Boolean = false,
    val showClickToSearch: Boolean = false,
) : ViewState