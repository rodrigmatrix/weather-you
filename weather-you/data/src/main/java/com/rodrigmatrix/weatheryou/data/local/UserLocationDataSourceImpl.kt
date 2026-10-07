package com.rodrigmatrix.weatheryou.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.SystemClock
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import org.joda.time.DateTime
import java.util.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class UserLocationDataSourceImpl(
    private val context: Context,
    private val locationServices: FusedLocationProviderClient,
    private val locationManager: LocationManager,
    private val geoCoder: Geocoder,
) : UserLocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Flow<CurrentLocation> {
        return flow {
            val location = getRecentLocationManagerLocation() ?: locationServices.lastLocation.await()
                ?.takeIf {
                    LocationFreshnessPolicy.isRecent(
                        it.elapsedRealtimeNanos,
                        SystemClock.elapsedRealtimeNanos(),
                    )
                }
                ?: throw CurrentLocationNotFoundException()
            val address = withTimeoutOrNull(GEOCODER_TIMEOUT_MILLIS) {
                getGeocoderLocation(location).firstOrNull()
            } ?: throw CurrentLocationNotFoundException()
            emit(location.toCurrentLocation(address))
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<CurrentLocation> {
        return flow {
            val location = getRecentLocationManagerLocation()
                ?: getCurrentPlayServicesLocation()
                ?: throw CurrentLocationNotFoundException()
            val address = withTimeoutOrNull(GEOCODER_TIMEOUT_MILLIS) {
                getGeocoderLocation(location).firstOrNull()
            }
            emit(location.toCurrentLocation(address))
        }.catch {
            if (it is CancellationException) throw it
            throw CurrentLocationNotFoundException()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentPlayServicesLocation(): Location? {
        val cancellationTokenSource = CancellationTokenSource()
        return try {
            withTimeoutOrNull(LOCATION_REQUEST_TIMEOUT_MILLIS + LOCATION_REQUEST_TIMEOUT_GRACE_MILLIS) {
                locationServices.getCurrentLocation(
                    CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setMaxUpdateAgeMillis(LocationFreshnessPolicy.MAX_CACHED_LOCATION_AGE_MILLIS)
                        .setDurationMillis(LOCATION_REQUEST_TIMEOUT_MILLIS)
                        .build(),
                    cancellationTokenSource.token,
                ).await(cancellationTokenSource)
            }
        } finally {
            cancellationTokenSource.cancel()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getRecentLocationManagerLocation(): Location? {
        return LocationFreshnessPolicy.newestRecent(
            locations = getLocationManagerLocations(),
            nowElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos(),
            timestamp = Location::getElapsedRealtimeNanos,
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocationManagerLocations(): List<Location> {
        val providers = buildList {
            add(LocationManager.GPS_PROVIDER)
            add(LocationManager.NETWORK_PROVIDER)
            add(LocationManager.PASSIVE_PROVIDER)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) add(LocationManager.FUSED_PROVIDER)
        }
        return providers.mapNotNull { provider ->
            try {
                locationManager.getLastKnownLocation(provider)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }

    private fun Location.toCurrentLocation(address: Address?): CurrentLocation {
        val addressName = address?.let {
            listOfNotNull(it.subAdminArea, it.adminArea, it.countryName)
                .map(String::trim)
                .filter(String::isNotEmpty)
                .joinToString(",")
                .takeIf(String::isNotBlank)
        }
        val fallbackName = context.getString(R.string.current_location).trim()
        return CurrentLocation(
            name = CurrentLocationFallbackPolicy.resolveName(addressName, fallbackName),
            latitude = latitude,
            longitude = longitude,
            countryCode = address?.countryCode.orEmpty(),
            timezone = TimeZone.getDefault().id,
            lastUpdate = DateTime.now(),
        )
    }

    private fun getGeocoderLocation(location: Location): Flow<Address?> {
        return callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val listener = Geocoder.GeocodeListener { addresses ->
                    trySend(addresses.firstOrNull())
                    close()
                }
                geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1,
                    listener
                )
            } else {
                try {
                    @Suppress("DEPRECATION")
                    val addresses = geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1,
                    )
                    send(addresses?.firstOrNull())
                    close()
                } catch (e: Exception) {
                    close(e)
                }
            }
            awaitClose { }
        }.catch {
            emit(null)
        }
    }

    private companion object {
        const val LOCATION_REQUEST_TIMEOUT_MILLIS = 10 * 1000L
        const val LOCATION_REQUEST_TIMEOUT_GRACE_MILLIS = 2 * 1000L
        const val GEOCODER_TIMEOUT_MILLIS = 5 * 1000L
    }
}
