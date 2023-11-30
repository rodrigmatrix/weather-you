package com.rodrigmatrix.weatheryou.tv.presentation.search

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.usecase.AddLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
internal class SearchLocationViewModel(
    private val addLocationUseCase: AddLocationUseCase,
    private val getFamousLocationsUseCase: GetFamousLocationsUseCase,
    private val searchLocationUseCase: SearchLocationUseCase,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<SearchLocationUiState, SearchLocationUiAction>(SearchLocationUiState()) {

    private val querySearch = Channel<String>()

    init {
        getFamousLocations()
        viewModelScope.launch {
            querySearch
                .receiveAsFlow()
                .onEach { query ->
                    setState { it.copy(searchText = query, isLoading = true) }
                }
                .debounce(600L)
                .collect { query ->
                    searchLocationUseCase(query)
                        .flowOn(coroutineDispatcher)
                        .catch { exception ->
                            exception.logError()
                        }
                        .collect { locations ->
                            setState { it.copy(locationsList = locations, isLoading = false) }
                        }
                }
        }
    }

    fun addLocation(location: SearchAutocompleteLocation?) {
        viewModelScope.launch {
            if (location != null) {
                addLocationUseCase(
                    location.name,
                    location.lat,
                    location.long,
                ).flowOn(coroutineDispatcher)
                    .onStart { setState { it.copy(isLoading = true) } }
                    .catch { exception ->
                        exception.handleError()
                    }
                    .collect { setEffect { SearchLocationUiAction.LocationAdded } }
            }
        }
    }

    fun addFamousLocation(city: City, context: Context) {
        viewModelScope.launch {
            addLocationUseCase(
                context.getString(city.name),
                city.lat,
                city.long,
            ).flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { setEffect { SearchLocationUiAction.LocationAdded } }
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
                    setState { it.copy(famousLocationsList = famousLocationsList) }
                }
        }
    }

    fun onSearch(searchText: String) {
        querySearch.trySend(searchText)
    }

    private fun Throwable.handleError() {
        firebaseCrashlytics.recordException(this)
        val exception = when (this) {
            is LocationLimitException -> R.string.add_location_limit_error
            else -> R.string.add_location_error
        }
        setEffect { SearchLocationUiAction.ShowError(exception) }
        setState { it.copy(isLoading = false) }
    }

    private fun Throwable.logError() {
        firebaseCrashlytics.recordException(this)
        setEffect { SearchLocationUiAction.ShowErrorString(this.toString()) }
        setState { it.copy(isLoading = false) }
    }
}
