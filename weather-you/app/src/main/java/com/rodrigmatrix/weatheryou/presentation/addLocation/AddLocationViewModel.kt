package com.rodrigmatrix.weatheryou.presentation.addLocation

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
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
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

    fun addLocation(location: String) {
        viewModelScope.launch {
            weatherRepository.addLocation(location)
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch { exception ->
                    setEffect { ShowError(exception.message.orEmpty()) }
                }
                .collect {
                    setEffect { LocationAdded }
                }
        }
    }

    fun onSearch(searchText: String) {
        setState {
            it.copy(searchText = searchText, locationsList = listOf(searchText))
        }
    }
}
