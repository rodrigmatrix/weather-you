package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import java.util.TimeZone

class UserLocationDataSourceImpl(
    private val locationServices: FusedLocationProviderClient,
    private val geoCoder: Geocoder
) : UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Flow<CurrentLocation> {
        return flow {
            val location = locationServices
                .lastLocation
                .await() ?: throw CurrentLocationNotFoundException()

            val address = geoCoder
                .getFromLocation(location.latitude, location.longitude, 1)
                ?.firstOrNull() ?: throw CurrentLocationNotFoundException()
            emit(address.toCurrentLocation())
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<CurrentLocation> {
        return flow {
            val location = locationServices
                .getCurrentLocation(
                    LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationRequest
                )
                .await() ?: throw CurrentLocationNotFoundException()

            val address = geoCoder
                .getFromLocation(location.latitude, location.longitude, 1)
                ?.firstOrNull() ?: throw CurrentLocationNotFoundException()
            emit(address.toCurrentLocation())
        }
    }

    private fun Address.toCurrentLocation(): CurrentLocation {
        return CurrentLocation(
            name = "$subAdminArea,$adminArea,$countryName",
            latitude = this.latitude,
            longitude = this.longitude,
            countryCode = this.countryCode,
            timezone = TimeZone.getDefault().id,
            lastUpdate = DateTime.now(),
        )
    }

    private val cancellationRequest = object : CancellationToken() {
        override fun isCancellationRequested(): Boolean {
            return false
        }

        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
            return this
        }
    }
}