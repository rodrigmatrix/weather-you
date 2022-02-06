package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserLocationDataSourceImpl(
    private val locationServices: FusedLocationProviderClient,
    private val geoCoder: Geocoder
): UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<String> {
        return flow {
            val location = Tasks.await(locationServices.lastLocation)
            val address = geoCoder
                .getFromLocation(location.latitude, location.longitude, 1)
                .firstOrNull()
            emit(address?.formatLocationName().orEmpty())
        }.catch { emit("") }
    }

    private fun Address.formatLocationName(): String {
        return "$subAdminArea,$adminArea,$countryName"
    }
}