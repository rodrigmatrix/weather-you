package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.mapper.FamousCitiesMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.Cities
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val searchRemoteDataSource: SearchRemoteDataSource,
    private val famousCitiesMapper: FamousCitiesMapper
): SearchRepository {

    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return searchRemoteDataSource.searchLocation(locationName)
    }

    override fun getLocation(placeId: String): Flow<SearchLocation> {
        return searchRemoteDataSource.getLocation(placeId)
    }

    override fun getFamousCities(): Flow<List<City>> {
        return flow {
            emit(Cities.values().let { famousCitiesMapper.map(it.toList()) })
        }
    }
}