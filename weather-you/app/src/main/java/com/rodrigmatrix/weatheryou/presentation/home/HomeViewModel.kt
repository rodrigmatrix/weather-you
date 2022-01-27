package com.rodrigmatrix.weatheryou.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()) {

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            weatherRepository.getLocationsList()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .collect { weatherLocationsList ->
                    setState { it.copy(locationsList = weatherLocationsList) }
                }
        }
    }
}