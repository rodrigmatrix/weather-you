package com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import com.rodrigmatrix.weatheryou.wearos.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<HomeViewState, HomeViewEffect>(HomeViewState()) {

    init {
        loadLocations()
        fetchLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true, error = null) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { weatherLocations ->
                    val initialPages = mutableListOf<HomePage>(HomePage.Settings)
                    if (weatherLocations.isEmpty()) {
                        initialPages.add(HomePage.EmptyState)
                    }
                    val pages = initialPages + weatherLocations.map { weatherLocation ->
                        HomePage.Weather(weatherLocation)
                    }
                    val oldPages = viewState.value.pages
                    setState {
                        it.copy(
                            isLoading = false,
                            error = null,
                            weatherLocations = weatherLocations,
                            pages = pages,
                        )
                    }
                    if (
                        (oldPages.size != pages.size && oldPages.isNotEmpty()) ||
                        (oldPages.isNotEmpty() && oldPages.none { it is HomePage.Weather })
                    ) {
                        delay(400L)
                        setEffect { HomeViewEffect.ScrollPager(pages.size - 1) }
                    }
                }
        }
    }

    fun fetchLocations() {
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


