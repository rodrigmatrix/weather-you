package com.rodrigmatrix.weatheryou.presentation.widget

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
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
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getWidgetTemperatureUseCase: GetWidgetTemperatureUseCase,
    private val setWidgetLocationUseCase: SetWidgetLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<CurrentWeatherWidgetConfigurationUiState, CurrentWeatherWidgetConfigurationUiAction>(
    CurrentWeatherWidgetConfigurationUiState()
) {

    fun getLocations(widgetId: String) {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch {
                    it
                }
                .combine(getWidgetTemperatureUseCase(widgetId)) { weatherLocationsList, savedWeather ->
                    setState {
                        it.copy(
                            isLoading = false,
                            locationsList = weatherLocationsList,
                            selectedLocation = weatherLocationsList.find { location ->
                                location.latitude == savedWeather?.latitude &&
                                        location.longitude == savedWeather.longitude
                            } ?: weatherLocationsList.firstOrNull()
                        )
                    }
                }
                .collect{ }
        }
    }

    fun setLocation(weatherLocation: WeatherLocation, widgetId: String) {
        viewModelScope.launch {
            setWidgetLocationUseCase(weatherLocation, widgetId)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .collect {
                    setEffect { CurrentWeatherWidgetConfigurationUiAction.OnConfigurationCompleted }
                }
        }
    }
}