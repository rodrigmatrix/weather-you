package com.rodrigmatrix.weatheryou.home.presentation.home

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshingLocations: Boolean = false,
    val locationsList: List<WeatherLocation> = emptyList(),
    val selectedWeatherLocation: WeatherLocation? = null,
    val deleteLocationDialogVisible: Boolean = false,
    val enableWeatherAnimations: Boolean = false,
    val enableThemeColorWithWeatherAnimations: Boolean = false,
): ViewState {

    fun isLocationSelected(): Boolean {
        return selectedWeatherLocation != null
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun showLocationPermissionRequest(permissionState: MultiplePermissionsState): Boolean {
        return permissionState.allPermissionsGranted.not()
                && permissionState.shouldShowRationale.not()
                && locationsList.isEmpty()
    }
}