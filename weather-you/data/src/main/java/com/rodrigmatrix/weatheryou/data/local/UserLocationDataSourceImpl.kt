package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Tasks
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class UserLocationDataSourceImpl(
    private val locationServices: FusedLocationProviderClient,
    private val geoCoder: Geocoder
) : UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<CurrentLocation> {
        return flow {
            val location = locationServices.lastLocation.await() ?: throw CurrentLocationNotFoundException()

            val address = geoCoder
                .getFromLocation(location.latitude, location.longitude, 1)
                .firstOrNull() ?: throw CurrentLocationNotFoundException()
            emit(address.toCurrentLocation())
        }
    }

    private fun Address.toCurrentLocation(): CurrentLocation {
        return CurrentLocation(
            name = "$subAdminArea,$adminArea,$countryName",
            latitude = this.latitude,
            longitude = this.longitude
        )
    }
}