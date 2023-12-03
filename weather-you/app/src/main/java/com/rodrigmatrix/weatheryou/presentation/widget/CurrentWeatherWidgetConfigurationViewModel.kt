package com.rodrigmatrix.weatheryou.presentation.widget

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.extensions.catch
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.FetchLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetWidgetLocationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CurrentWeatherWidgetConfigurationViewModel(
    private val fetchLocationsUseCase: FetchLocationsUseCase,
    private val getWidgetTemperatureUseCase: GetWidgetTemperatureUseCase,
    private val setWidgetLocationUseCase: SetWidgetLocationUseCase,
    private val deleteWidgetLocationUseCase: DeleteWidgetLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<CurrentWeatherWidgetConfigurationUiState, CurrentWeatherWidgetConfigurationUiAction>(
    CurrentWeatherWidgetConfigurationUiState()
) {

    init {
        getLocations()
    }

    private fun getLocations() {
        viewModelScope.launch {
            fetchLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch()
                .onStart { setState { it.copy(isLoading = true) } }
                .combine(getWidgetTemperatureUseCase()) { weatherLocationsList, savedWeather ->
                    setState {
                        it.copy(
                            isLoading = false,
                            locationsList = weatherLocationsList,
                            selectedLocation = weatherLocationsList.find { location ->
                                location.latitude == savedWeather?.lat &&
                                        location.longitude == savedWeather.long
                            } ?: weatherLocationsList.firstOrNull()
                        )
                    }
                }
                .collect { }
        }
    }

    fun setLocation(weatherLocation: WeatherLocation) {
        viewModelScope.launch {
//            if (weatherLocation.isCurrentLocation) {
//                deleteWidgetLocationUseCase()
//                    .flowOn(coroutineDispatcher)
//                    .onStart { setState { it.copy(isLoading = true) } }
//                    .collect {
//                        setEffect { CurrentWeatherWidgetConfigurationUiAction.OnConfigurationCompleted }
//                    }
//                return@launch
//            }
            setWidgetLocationUseCase(weatherLocation)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .collect {
                    setEffect { CurrentWeatherWidgetConfigurationUiAction.OnConfigurationCompleted }
                }
        }
    }
}