package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.data.service.LocationIqService
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchRemoteDataSourceImpl(
    private val locationIqService: LocationIqService,
) : SearchRemoteDataSource {

    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return flow {
            emit(
                locationIqService.searchLocation(locationName)
                    .map {
                        val city = it.address?.name.orEmpty()
                        val state = it.address?.state.orEmpty()
                        val country = it.address?.country.orEmpty()
                        SearchAutocompleteLocation(
                            name = if (city.isNotEmpty() && state.isNotEmpty()) {
                                "$city, $state, $country"
                            } else {
                                it.displayName.orEmpty()
                            },
                            lat = it.lat?.toDouble() ?: 0.0,
                            long = it.lon?.toDouble() ?: 0.0,
                            countryCode = it.address?.countryCode.orEmpty(),
                            timezone = "",
                        )
                    }
            )
        }
    }

    override fun getTimezone(lat: Double, long: Double): Flow<String> {
        return flow { emit(locationIqService.getTimezone(lat, long)) }
            .map { it.timezone?.name.orEmpty() }
            .catch {
                emit("")
            }
    }


}