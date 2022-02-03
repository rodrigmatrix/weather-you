package com.rodrigmatrix.weatheryou.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()) {

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            weatherRepository.fetchLocationsList()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception -> setState { it.copy(error = exception) } }
                .collect { weatherLocationsList ->
                    setState {
                        it.copy(
                            locationsList = weatherLocationsList,
                            error = null,
                            isLoading = false
                        )
                    }
                }
        }
    }
}