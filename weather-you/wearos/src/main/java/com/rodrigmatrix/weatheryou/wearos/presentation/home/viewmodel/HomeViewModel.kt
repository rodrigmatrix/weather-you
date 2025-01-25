package com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.usecase.AddLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.domain.usecase.GetLocationWeatherUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, ViewEffect>(HomeViewState()) {

    init {
        loadLocation()
        fetchLocations()
    }

    fun loadLocation() {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true, error = null) } }
                .catch { exception ->
                    exception.handleError()
                }
                .onEach { weatherLocations ->
                    setState {
                        it.copy(
                            isLoading = false,
                            error = null,
                            weatherLocations = weatherLocations,
                        )
                    }
                }.collect()
        }
    }

    private fun fetchLocations() {
        viewModelScope.launch {
            updateLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    exception.handleError()
                }.collect()
        }
    }

    private fun Throwable.handleError() {
        val errorString = when (this) {
            is CurrentLocationNotFoundException -> R.string.current_location_not_found
            is IOException -> R.string.no_internet_connect
            else -> R.string.generic_error
        }
        setState { it.copy(error = errorString, isLoading = false) }
    }
}


