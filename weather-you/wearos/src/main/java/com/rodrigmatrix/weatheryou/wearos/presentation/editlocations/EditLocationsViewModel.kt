package com.rodrigmatrix.weatheryou.wearos.presentation.editlocations

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EditLocationsViewModel(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val updateLocationsUseCase: UpdateLocationsUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<EditLocationsViewState, EditLocationsViewEffect>(EditLocationsViewState()) {


    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            getLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .collect { locations ->
                    setState { it.copy(weatherLocations = locations) }
                }
        }
    }

    fun showDeleteLocation(weatherLocation: WeatherLocation?) {
        setState { it.copy(deletingLocation = weatherLocation) }
    }

    fun deleteLocation() {
        viewModelScope.launch {
            deleteLocationUseCase(viewState.value.deletingLocation!!)
                .flowOn(coroutineDispatcher)
                .collect {
                    setState { it.copy(deletingLocation = null) }
                }
        }
    }
}