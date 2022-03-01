package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.mapper.FamousCitiesMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.Cities
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSource
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val famousCitiesMapper: FamousCitiesMapper
): SearchRepository {

    override fun getFamousCities(): Flow<List<City>> {
        return flow {
            emit(Cities.values().let { famousCitiesMapper.map(it.toList()) })
        }
    }
}