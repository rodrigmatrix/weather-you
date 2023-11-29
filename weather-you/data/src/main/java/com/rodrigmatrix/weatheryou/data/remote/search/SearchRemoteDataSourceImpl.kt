package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.data.mapper.SearchAutocompleteRemoteMapper
import com.rodrigmatrix.weatheryou.data.mapper.SearchLocationRemoteMapper
import com.rodrigmatrix.weatheryou.data.service.ApiNinjasService
import com.rodrigmatrix.weatheryou.data.service.SearchLocationService
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Locale

class SearchRemoteDataSourceImpl(
    private val apiNinjasService: ApiNinjasService,
    private val searchAutocompleteRemoteMapper: SearchAutocompleteRemoteMapper,
    private val searchLocationRemoteMapper: SearchLocationRemoteMapper,
) : SearchRemoteDataSource {

    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return flow {
            emit(apiNinjasService.searchCity(locationName))
        }.map(searchAutocompleteRemoteMapper::map)
    }
}