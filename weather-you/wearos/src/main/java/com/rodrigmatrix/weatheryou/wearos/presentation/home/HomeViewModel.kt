package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.domain.usecase.GetCurrentLocationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, ViewEffect>(HomeViewState()) {

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            getCurrentLocationUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { weatherLocation ->
                    setState {
                        it.copy(
                            isLoading = false,
                            error = null,
                            weatherLocation = weatherLocation
                        )
                    }
                }
        }
    }

    private fun Throwable.handleError() {
        val errorString = when (this) {
            is CurrentLocationNotFoundException -> R.string.current_location_not_found
            else -> R.string.generic_error
        }
        setState { it.copy(error = errorString, isLoading = false) }
    }
}


