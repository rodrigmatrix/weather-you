package com.rodrigmatrix.weatheryou.wearos.presentation.location

import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.usecase.AddLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationViewEffect.LocationAdded
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationViewEffect.ShowError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val addLocationUseCase: AddLocationUseCase,
    private val getFamousLocationsUseCase: GetFamousLocationsUseCase,
    private val searchLocationUseCase: SearchLocationUseCase,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

    init {
        getFamousLocations()
    }

    fun addLocation(location: SearchAutocompleteLocation?) {
        setState { it.copy(isAddingLocation = true) }
        viewModelScope.launch {
            if (location != null) {
                addLocationUseCase(
                    location.name,
                    location.lat,
                    location.long,
                    location.countryCode,
                ).flowOn(coroutineDispatcher)
                    .catch { exception ->
                        exception.handleError()
                    }
                    .collect {
                        firebaseAnalytics.logEvent("ADDED_LOCATION", bundleOf("name" to location.name))
                        setEffect { LocationAdded }
                        setState { AddLocationViewState() }
                        getFamousLocations()
                    }
            }
        }
    }

    fun addFamousLocation(city: City, context: Context) {
        setState { it.copy(isAddingLocation = true) }
        viewModelScope.launch {
            addLocationUseCase(
                context.getString(city.name),
                city.lat,
                city.long,
                city.countryCode,
            ).flowOn(coroutineDispatcher)
                .catch { exception ->
                    firebaseAnalytics.logEvent("ADD_FAMOUS_LOCATION_ERROR", bundleOf("error" to exception.localizedMessage))
                    exception.handleError()
                }
                .collect {
                    firebaseAnalytics.logEvent("ADDED_FAMOUS_LOCATION", bundleOf("countryCode" to city.countryCode))
                    setEffect { LocationAdded }
                    setState { AddLocationViewState() }
                    getFamousLocations()
                }
        }
    }

    private fun getFamousLocations() {
        viewModelScope.launch {
            getFamousLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    exception.logError()
                }
                .collect { famousLocationsList ->
                    setState { it.copy(famousLocations = famousLocationsList) }
                }
        }
    }

    fun onQueryChanged(searchText: String) {
        setState {
            it.copy(
                searchText = searchText,
                searchHelperText = when {
                    searchText.length < 3 && searchText.isNotEmpty() -> "Keep Typing"
                    searchText.length >= 3 -> "Tap search icon to search"
                    else -> ""
                },
            )
        }
    }

    fun search() {
        viewModelScope.launch {
            searchLocationUseCase(viewState.value.searchText)
                .flowOn(coroutineDispatcher)
                .onStart {
                    setState {
                        it.copy(isLoadingLocations = true)
                    }
                }
                .catch { exception ->
                    firebaseAnalytics.logEvent("ERROR_SEARCHED_LOCATION", bundleOf("error" to exception.localizedMessage))
                    exception.logError()
                }
                .collect { locations ->
                    firebaseAnalytics.logEvent("SEARCHED_LOCATION", bundleOf("name" to viewState.value.searchText))
                    setState {
                        it.copy(
                            searchedLocations = locations,
                            showEmptyState = locations.isEmpty(),
                            showClickToSearch = false,
                            isLoadingLocations = false,
                        )
                    }
                }
        }
    }

    private fun Throwable.handleError() {
        firebaseCrashlytics.recordException(this)
        firebaseAnalytics.logEvent("ADD_LOCATION_ERROR", bundleOf("error" to this.localizedMessage))
        val exception = when (this) {
            is LocationLimitException -> R.string.add_location_limit_error
            else -> R.string.add_location_error
        }
        setEffect { ShowError(exception) }
        setState { it.copy(isLoadingLocations = false, isAddingLocation = false) }
    }

    private fun Throwable.logError() {
        firebaseCrashlytics.recordException(this)
        setEffect { AddLocationViewEffect.ShowErrorString(this.toString()) }
        setState { it.copy(isLoadingLocations = false, isAddingLocation = false) }
    }
}