package com.rodrigmatrix.weatheryou.presentation.addLocation

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewEffect.LocationAdded
import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewEffect.ShowError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val weatherRepository: WeatherRepository,
    private val getFamousLocationsUseCase: GetFamousLocationsUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

    init {
        getFamousLocations()
    }

    fun addLocation(location: String) {
        viewModelScope.launch {
            weatherRepository.addLocation(location)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch { exception ->
                    setEffect { ShowError(exception.message.orEmpty()) }
                    setState { it.copy(isLoading = false) }
                }
                .collect {
                    setEffect { LocationAdded }
                }
        }
    }

    private fun getFamousLocations() {
        viewModelScope.launch {
            getFamousLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    setEffect { ShowError(exception.message.orEmpty()) }
                    setState { it.copy(famousLocationsList = emptyList()) }

                }
                .collect { famousLocationsList ->
                    setState { it.copy(famousLocationsList = famousLocationsList) }
                }
        }
    }

    fun onSearch(searchText: String) {
        setState {
            it.copy(
                searchText = searchText,
                locationsList = listOf(searchText).filter { name -> name.isNotEmpty() }
            )
        }
    }
}
