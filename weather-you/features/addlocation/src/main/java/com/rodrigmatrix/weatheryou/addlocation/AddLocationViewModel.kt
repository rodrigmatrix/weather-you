package com.rodrigmatrix.weatheryou.addlocation

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.usecase.AddLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewEffect.LocationAdded
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewEffect.ShowError
import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val addLocationUseCase: AddLocationUseCase,
    private val getFamousLocationsUseCase: GetFamousLocationsUseCase,
    private val searchLocationUseCase: SearchLocationUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

    init {
        getFamousLocations()
    }

    fun addLocation(placeId: String) {
        viewModelScope.launch {
            getLocationUseCase(placeId)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { searchLocation ->
                    addLocationUseCase(
                        searchLocation.name,
                        searchLocation.latitude,
                        searchLocation.longitude
                    ).flowOn(coroutineDispatcher)
                        .onStart { setState { it.copy(isLoading = true) } }
                        .catch { exception ->
                            exception.handleError()
                        }
                        .collect { setEffect { LocationAdded } }
                }
        }
    }

    private fun getFamousLocations() {
        viewModelScope.launch {
            getFamousLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    exception.handleError()
                }
                .collect { famousLocationsList ->
                    setState { it.copy(famousLocationsList = famousLocationsList) }
                }
        }
    }

    fun onSearch(searchText: String) {
        viewModelScope.launch {
            searchLocationUseCase(searchText)
                .flowOn(coroutineDispatcher)
                .onStart {
                    setState { it.copy(searchText = searchText, isLoading = true) }
                }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { locations ->
                    setState { it.copy(locationsList = locations, isLoading = false) }
                }
        }
    }

    private fun Throwable.handleError() {
        val exception = when (this) {
            is LocationLimitException -> R.string.add_location_limit_error
            else -> R.string.add_location_error
        }
        setEffect { ShowError(exception) }
        setState { it.copy(isLoading = false) }
    }
}
