package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.mapper.FamousCitiesMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.Cities
import com.rodrigmatrix.weatheryou.data.remote.search.SearchLocalDataSource
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(
    private val searchLocalDataSource: SearchLocalDataSource,
    private val searchRemoteDataSource: SearchRemoteDataSource,
    private val famousCitiesMapper: FamousCitiesMapper,
): SearchRepository {

    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return searchRemoteDataSource.searchLocation(locationName).map {
            it.map { location ->
                SearchAutocompleteLocation(
                    name = location.name,
                    lat = location.lat,
                    long = location.long,
                    countryCode = location.countryCode,
                    timezone = location.timezone,
                )
            }
        }.catch {
            searchLocalDataSource.searchLocation(locationName).map {
                it.map { location ->
                    SearchAutocompleteLocation(
                        name = "${location.city} - ${location.state} ${location.country}",
                        lat = location.lat,
                        long = location.long,
                        countryCode = location.countryCode,
                        timezone = "",
                    )
                }
            }
        }
    }

    override fun getFamousCities(): Flow<List<City>> {
        return flow {
            emit(Cities.entries.let { famousCitiesMapper.map(it.toList()) })
        }
    }

    override fun getTimezone(lat: Double, long: Double): Flow<String> {
        return searchRemoteDataSource.getTimezone(lat, long)
    }
}