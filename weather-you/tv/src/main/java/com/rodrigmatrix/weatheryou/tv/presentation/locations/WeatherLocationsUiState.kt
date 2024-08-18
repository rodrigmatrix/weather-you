package com.rodrigmatrix.weatheryou.tv.presentation.locations

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class WeatherLocationsUiState(
    val isLoading: Boolean = false,
    val locationsList: List<WeatherLocation> = emptyList(),
    val selectedWeatherLocation: WeatherLocation? = null,
    val deleteLocationDialogVisible: Boolean = false,
): ViewState {

    @OptIn(ExperimentalPermissionsApi::class)
    fun showLocationPermissionRequest(permissionState: PermissionState): Boolean {
        return permissionState.hasPermission.not()
                && permissionState.permissionRequested.not()
                && locationsList.isEmpty()
                && isLoading.not()
    }
}