package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Tasks
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class UserLocationDataSourceImpl(
    private val locationServices: FusedLocationProviderClient,
    private val geoCoder: Geocoder
) : UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<CurrentLocation> {
        return flow {
            val location = Tasks.await(
                locationServices.lastLocation,
                4000L,
                TimeUnit.MILLISECONDS
            )
            val address = geoCoder
                .getFromLocation(location.latitude, location.longitude, 1)
                .first()
            emit(address.toCurrentLocation())
        }.catch {  }
    }

    private fun Address.toCurrentLocation(): CurrentLocation {
        return CurrentLocation(
            name = "$subAdminArea,$adminArea,$countryName",
            latitude = this.latitude,
            longitude = this.longitude
        )
    }
}