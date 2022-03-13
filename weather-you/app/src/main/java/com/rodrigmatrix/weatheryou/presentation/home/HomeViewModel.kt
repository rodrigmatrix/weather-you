package com.rodrigmatrix.weatheryou.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.presentation.home.HomeViewEffect.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()) {

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            weatherRepository.fetchLocationsList()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    setEffect { Error(exception.message.orEmpty()) }
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

    private fun getSelectedWeatherLocation(locationsList: List<com.rodrigmatrix.weatheryou.domain.model.WeatherLocation>): com.rodrigmatrix.weatheryou.domain.model.WeatherLocation? {
        return locationsList.find { location ->
            location.name == viewState.value.selectedWeatherLocation?.name
        }
    }

    fun deleteLocation(weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation) {
        viewModelScope.launch {
            weatherRepository.deleteLocation(weatherLocation)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    setEffect { Error(exception.message.orEmpty()) }
                }
                .collect {
                    setState {
                        it.copy(isLoading = false, selectedWeatherLocation = null)
                    }
                }
        }
    }

    fun selectLocation(weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation? = null) {
        setState {
            it.copy(selectedWeatherLocation = getSelectedLocation(weatherLocation))
        }
    }

    private fun getSelectedLocation(weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation?): com.rodrigmatrix.weatheryou.domain.model.WeatherLocation? {
        return if (weatherLocation == viewState.value.selectedWeatherLocation) {
            null
        } else {
            weatherLocation
        }
    }

    fun onCloseClicked() {
        setState { it.copy(selectedWeatherLocation = null) }
    }
}