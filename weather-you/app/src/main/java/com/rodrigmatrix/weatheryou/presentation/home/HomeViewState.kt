package com.rodrigmatrix.weatheryou.presentation.home

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class HomeViewState(
    val isLoading: Boolean = false,
    val locationsList: List<com.rodrigmatrix.weatheryou.domain.model.WeatherLocation> = emptyList(),
    val selectedWeatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation? = null,
    val deletePackageDialogVisible: Boolean = false
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState {

    fun isLocationSelected(): Boolean {
        return selectedWeatherLocation != null
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun showLocationPermissionRequest(permissionState: PermissionState): Boolean {
        return permissionState.hasPermission.not()
                && permissionState.permissionRequested.not()
                && locationsList.isEmpty()
                && isLoading.not()
    }
}