package com.rodrigmatrix.weatheryou.tv.presentation.locations

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationSizeUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

class TVWeatherLocationsViewModel(
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<TvWeatherLocationsUiState, TvWeatherLocationsUiAction>(TvWeatherLocationsUiState()) {

    init {
        updateLocations()
        observeLocations()
    }

    private fun observeLocations() {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .collect { locations ->
                    setState {
                        it.copy(
                            locationsList = locations,
                            locationsSize = locations.size,
                        )
                    }
                }
        }
    }

    fun updateLocations() {
        viewModelScope.launch {
            updateLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { weatherLocationsList ->
                    setState {
                        it.copy(isLoading = false)
                    }
                }
        }
    }

    private fun getSelectedWeatherLocation(locationsList: List<WeatherLocation>): WeatherLocation? {
        return locationsList.find { location ->
            location.name == viewState.value.selectedWeatherLocation?.name
        }
    }

    fun showDeleteLocationDialog() {
        setState { it.copy(deleteLocationDialogVisible = true) }
    }

    fun hideDeleteLocationDialog() {
        setState { it.copy(deleteLocationDialogVisible = false) }
    }

    fun deleteLocation(navigationType: ScreenNavigationType) {
        viewState.value.selectedWeatherLocation?.let {
            deleteLocation(it, navigationType)
        }
    }

    fun deleteLocation(location: WeatherLocation) {
        deleteLocation(location, ScreenNavigationType.NAVIGATION_RAIL)
    }

    fun deleteLocation(location: WeatherLocation, navigationType: ScreenNavigationType) {
        viewModelScope.launch {
            hideDeleteLocationDialog()
            deleteLocationUseCase(location)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect {
                    setState {
                        if (it.selectedWeatherLocation == location &&
                            navigationType == ScreenNavigationType.NAVIGATION_RAIL
                        ) {
                            it.copy(
                                isLoading = false,
                                selectedWeatherLocation = it.locationsList.first()
                            )
                        } else {
                            it.copy(
                                isLoading = false,
                                selectedWeatherLocation = null
                            )
                        }
                    }
                }
        }
    }

    fun selectLocation(weatherLocation: WeatherLocation? = null) {
        setState {
            it.copy(selectedWeatherLocation = getSelectedLocation(weatherLocation))
        }
    }

    private fun getSelectedLocation(weatherLocation: WeatherLocation?): WeatherLocation? {
        return if (weatherLocation == viewState.value.selectedWeatherLocation) {
            null
        } else {
            weatherLocation
        }
    }

    fun onCloseClicked() {
        setState { it.copy(selectedWeatherLocation = null) }
    }

    private fun Throwable.handleError() {
        val error = when(this) {
            is IOException -> R.string.internet_error
            else -> R.string.generic_error
        }
        setEffect { TvWeatherLocationsUiAction.Error(error) }
    }
}