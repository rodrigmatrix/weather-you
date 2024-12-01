package com.rodrigmatrix.weatheryou.tv.presentation.locations

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class TvWeatherLocationsUiState(
    val isLoading: Boolean = false,
    val locationsList: List<WeatherLocation> = emptyList(),
    val locationsSize: Int = 0,
    val selectedWeatherLocation: WeatherLocation? = null,
    val deleteLocationDialogVisible: Boolean = false,
): ViewState {

    @OptIn(ExperimentalPermissionsApi::class)
    fun showLocationPermissionRequest(permissionState: MultiplePermissionsState): Boolean {
        return permissionState.allPermissionsGranted.not()
                && permissionState.shouldShowRationale.not()
                && locationsList.isEmpty()
    }
}