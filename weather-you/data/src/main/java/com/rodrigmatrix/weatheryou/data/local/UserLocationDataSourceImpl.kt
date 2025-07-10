package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.joda.time.DateTime
import java.util.TimeZone

class UserLocationDataSourceImpl(
    private val locationServices: FusedLocationProviderClient,
    private val locationManager: LocationManager,
    private val geoCoder: Geocoder,
) : UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Flow<CurrentLocation> {
        return flow {
            val location = (getLocationManagerLocation() ?: getPlayServicesLocation().firstOrNull()) ?: throw CurrentLocationNotFoundException()
            emit(getGeocoderLocation(location).firstOrNull()?.toCurrentLocation() ?: throw CurrentLocationNotFoundException())
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<CurrentLocation> {
        return flow {
            val location = (getLocationManagerLocation() ?: getPlayServicesLocation().firstOrNull()) ?: throw CurrentLocationNotFoundException()
            emit(getGeocoderLocation(location).firstOrNull()?.toCurrentLocation() ?: throw CurrentLocationNotFoundException())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPlayServicesLocation(): Flow<Location?> {
        return flow {
            emit(
                withTimeout(10000) {
                    locationServices
                        .getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            cancellationRequest
                        )
                        .await()
                } ?: locationServices.lastLocation.await() ?: throw CurrentLocationNotFoundException()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationManagerLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?:
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) ?:
        locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) ?:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
        } else null
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

    private fun getGeocoderLocation(location: Location): Flow<Address?> {
        return callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1,
                ) { addresses ->
                    trySend(addresses.firstOrNull())
                    close()
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1,
                )
                send(addresses?.firstOrNull())
                close()
            }
        }
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