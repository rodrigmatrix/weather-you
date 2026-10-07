package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.data.service.LocationIqService
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import java.io.IOException
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val LOCATION_IQ_CALL_TIMEOUT_MILLIS = 15_000L

class SearchRemoteDataSourceImpl(
    private val locationIqService: LocationIqService,
    private val callTimeoutMillis: Long = LOCATION_IQ_CALL_TIMEOUT_MILLIS,
) : SearchRemoteDataSource {

    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return flow {
            val response = withTimeoutOrNull(callTimeoutMillis) {
                locationIqService.searchLocation(locationName)
            } ?: throw IOException("Location search request timed out")
            emit(
                response
                    .mapNotNull {
                        val latitude = it.lat?.toDoubleOrNull()
                            ?.takeIf { value -> value.isFinite() && value in -90.0..90.0 }
                            ?: return@mapNotNull null
                        val longitude = it.lon?.toDoubleOrNull()
                            ?.takeIf { value -> value.isFinite() && value in -180.0..180.0 }
                            ?: return@mapNotNull null
                        val city = it.address?.name.orEmpty()
                        val state = it.address?.state.orEmpty()
                        val country = it.address?.country.orEmpty()
                        SearchAutocompleteLocation(
                            name = if (city.isNotEmpty() && state.isNotEmpty()) {
                                "$city, $state, $country"
                            } else {
                                it.displayName.orEmpty()
                            },
                            lat = latitude,
                            long = longitude,
                            countryCode = it.address?.countryCode.orEmpty(),
                            timezone = "",
                        )
                    }
            )
        }
    }
    override fun getTimezone(lat: Double, long: Double): Flow<String> {
        return flow {
            val response = withTimeoutOrNull(callTimeoutMillis) {
                locationIqService.getTimezone(lat, long)
            }
            emit(response?.timezone?.name.orEmpty())
        }
            .catch {
                emit("")
            }
    }


}
