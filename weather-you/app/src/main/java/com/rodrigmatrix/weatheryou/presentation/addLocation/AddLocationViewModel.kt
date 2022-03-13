package com.rodrigmatrix.weatheryou.presentation.addLocation

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase
import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewEffect.LocationAdded
import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewEffect.ShowError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val weatherRepository: com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository,
    private val getFamousLocationsUseCase: com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase,
    private val searchLocationUseCase: com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase,
    private val getLocationUseCase: com.rodrigmatrix.weatheryou.domain.usecase.GetLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

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
                    weatherRepository.addLocation(
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
        val exception = this
        setEffect { ShowError(exception.message.orEmpty()) }
        setState { it.copy(isLoading = false) }
    }
}
