package com.rodrigmatrix.weatheryou.home.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.FetchLocationsUseCase
import com.rodrigmatrix.weatheryou.home.R
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewEffect.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val fetchLocationsUseCase: FetchLocationsUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()) {

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            fetchLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { weatherLocationsList ->
                    setState {
                        it.copy(
                            locationsList = weatherLocationsList,
                            selectedWeatherLocation = getSelectedWeatherLocation(weatherLocationsList),
                            isLoading = false
                        )
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
        setState { it.copy(deletePackageDialogVisible = true) }
    }

    fun hideDeleteLocationDialog() {
        setState { it.copy(deletePackageDialogVisible = false) }
    }

    fun deleteLocation() {
        viewModelScope.launch {
            hideDeleteLocationDialog()
            viewState.value.selectedWeatherLocation?.let { location ->
                deleteLocationUseCase(location.id)
                    .flowOn(coroutineDispatcher)
                    .onStart { setState { it.copy(isLoading = true) } }
                    .onCompletion { setState { it.copy(isLoading = false) } }
                    .catch { exception ->
                        exception.handleError()
                    }
                    .collect {
                        setState {
                            it.copy(isLoading = false, selectedWeatherLocation = null)
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
        setEffect { Error(error) }
    }
}