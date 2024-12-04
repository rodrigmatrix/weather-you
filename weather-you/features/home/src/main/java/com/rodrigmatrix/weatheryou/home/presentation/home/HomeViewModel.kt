package com.rodrigmatrix.weatheryou.home.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsListOrderUseCase
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewEffect.Error
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

private const val FIVE_MINUTES_MILLI = 300000L

class HomeViewModel(
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateLocationsListOrderUseCase: UpdateLocationsListOrderUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<HomeUiState, HomeViewEffect>(HomeUiState()) {

    private var updateCount = 0

    private var updateLocationsJob = SupervisorJob() + coroutineDispatcher

    init {
        loadLocations()
        updateLocations()
        viewModelScope.launch {
            getAppSettingsUseCase()
                .collect { settings ->
                    setState {
                        it.copy(
                            enableWeatherAnimations = settings.enableWeatherAnimations,
                            enableThemeColorWithWeatherAnimations = settings.enableThemeColorWithWeatherAnimations,
                        )
                    }
                }
        }
    }

    fun loadLocations() {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    exception.handleError()
                }
                .onStart { setState { it.copy(isLoading = true) } }
                .collect { weatherLocationsList ->
                    setState {
                        if (weatherLocationsList.size >= 2) {
                            setEffect { HomeViewEffect.ShowInAppReview }
                        }
                        it.copy(
                            locationsList = weatherLocationsList,
                            selectedWeatherLocation = getSelectedWeatherLocation(weatherLocationsList),
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun updateLocations() {
        updateLocationsJob.cancelChildren()
        updateCount = 0
        updateLocationsJob = viewModelScope.launch {
            while (updateCount < 5) {
                updateCount++
                setState { it.copy(isRefreshingLocations = true) }
                updateLocationsUseCase()
                    .flowOn(coroutineDispatcher)
                    .catch {
                        setState {
                            it.copy(
                                isRefreshingLocations = false,
                                isLoading = false,
                            )
                        }
                    }
                    .firstOrNull().let {
                        setState {
                            it.copy(
                                isLoading = false,
                                isRefreshingLocations = false
                            )
                        }
                        setEffect { HomeViewEffect.UpdateWidgets }
                    }
                delay(FIVE_MINUTES_MILLI)
            }
        }
    }

    private fun getSelectedWeatherLocation(locationsList: List<WeatherLocation>): WeatherLocation? {
        return locationsList.find { location ->
            location.name == viewState.value.selectedWeatherLocation?.name
        }
    }

    fun onDialogStateChanged(dialogState: HomeDialogState) {
        setState { it.copy(dialogState = dialogState) }
    }

    fun deleteLocation() {
        viewState.value.selectedWeatherLocation?.let {
            deleteLocation(it)
        }
    }

    fun orderLocations(list: List<WeatherLocation>) {
        viewModelScope.launch {
            updateLocationsListOrderUseCase(list)
                .flowOn(coroutineDispatcher)
                .catch { it.handleError() }
                .collect()
        }
    }

    fun deleteLocation(location: WeatherLocation) {
        viewModelScope.launch {
            onDialogStateChanged(HomeDialogState.Hidden)
            deleteLocationUseCase(location)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .onCompletion { setState { it.copy(isLoading = false) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect {
                    setState {
                        it.copy(
                            isLoading = false,
                            selectedWeatherLocation = null,
                        )
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

    private fun Throwable.handleError() {
        val error = when(this) {
            is IOException -> R.string.internet_error
            else -> R.string.generic_error
        }
        setEffect { Error(error) }
    }
}