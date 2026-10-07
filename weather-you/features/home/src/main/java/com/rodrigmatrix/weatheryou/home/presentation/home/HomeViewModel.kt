package com.rodrigmatrix.weatheryou.home.presentation.home

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationByLatLongUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsListOrderUseCase
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewEffect.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.collections.firstOrNull

private const val FIVE_MINUTES_MILLI = 300000L

class HomeViewModel(
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val getLocationByLatLongUseCase: GetLocationByLatLongUseCase,
    private val updateLocationsListOrderUseCase: UpdateLocationsListOrderUseCase,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userLocationDataSource: UserLocationDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<HomeUiState, HomeViewEffect>(HomeUiState()) {

    private var updateLocationsJob: Job? = null

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
                .onStart { setState { it.copy(isLoading = true) } }
                .collect { weatherLocationsList ->
                    firebaseAnalytics.logEvent("LOADED_LOCATIONS",
                        bundleOf(
                            "size" to weatherLocationsList.size,
                            "names" to weatherLocationsList.joinToString(separator = ",") { it.name },
                        )
                    )
                    setState {
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
        updateLocationsJob?.cancel()
        startLocationPolling(attempts = 5, delayBeforeFirstAttempt = false)
    }

    fun refreshLocations() {
        updateLocationsJob?.cancel()
        updateLocationsJob = viewModelScope.launch {
            updateLocationsOnce(
                forceUpdate = true,
                forceUpdateLocation = viewState.value.selectedWeatherLocation
                    ?: viewState.value.locationsList.firstOrNull(),
            )
            startLocationPolling(attempts = 4, delayBeforeFirstAttempt = true)
        }
    }

    private fun startLocationPolling(attempts: Int, delayBeforeFirstAttempt: Boolean) {
        updateLocationsJob = viewModelScope.launch {
            repeat(attempts) { refreshIndex ->
                if (delayBeforeFirstAttempt || refreshIndex > 0) delay(FIVE_MINUTES_MILLI)
                updateLocationsOnce(forceUpdate = false)
            }
        }
    }

    private suspend fun updateLocationsOnce(
        forceUpdate: Boolean,
        forceUpdateLocation: WeatherLocation? = null,
    ) {
        setState { it.copy(isRefreshingLocations = true) }
        var refreshSucceeded = true
        val result = updateLocationsUseCase(forceUpdate, forceUpdateLocation)
            .flowOn(coroutineDispatcher)
            .catch { exception ->
                refreshSucceeded = false
                firebaseAnalytics.logEvent("UPDATE_LOCATIONS_ERROR", bundleOf("error" to exception.localizedMessage))
            }
            .firstOrNull()
        refreshSucceeded = refreshSucceeded && result != null

        setState {
            it.copy(
                isLoading = false,
                isRefreshingLocations = false,
            )
        }
        if (refreshSucceeded) {
            firebaseAnalytics.logEvent("UPDATED_LOCATIONS", bundleOf())
            setEffect { HomeViewEffect.UpdateWidgets }
        }
    }
    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            setState { it.copy(isRefreshingLocations = true, isLoading = true) }
            updateLocations()
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
            it.copy(selectedWeatherLocation = weatherLocation)
        }
    }

    fun openLocation(
        latitude: Double,
        longitude: Double,
    ) {
        viewModelScope.launch {
            getLocationByLatLongUseCase(latitude, longitude)
                .catch { }
                .firstOrNull { weatherLocation ->
                    if (weatherLocation != null) {
                        setState {
                            it.copy(selectedWeatherLocation = weatherLocation)
                        }
                        setEffect {
                            HomeViewEffect.OpenLocation(
                                id = weatherLocation.id,
                                page = viewState.value.locationsList.indexOfFirst { it.id == weatherLocation.id },
                            )
                        }
                    }
                    return@firstOrNull true
                }

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
