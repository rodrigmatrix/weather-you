package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.data.mapper.SearchAutocompleteRemoteMapper
import com.rodrigmatrix.weatheryou.data.mapper.SearchLocationRemoteMapper
import com.rodrigmatrix.weatheryou.data.service.SearchLocationService
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class SearchRemoteDataSourceImpl(
    private val searchLocationService: SearchLocationService,
    private val searchAutocompleteRemoteMapper: SearchAutocompleteRemoteMapper,
    private val searchLocationRemoteMapper: SearchLocationRemoteMapper
) : SearchRemoteDataSource {

    @OptIn(FlowPreview::class)
    override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> {
        return flow {
            emit(searchLocationService.search(locationName, Locale.getDefault().toString()))
        }.debounce(3000)
            .map(searchAutocompleteRemoteMapper::map)
    }

    override fun getLocation(placeId: String): Flow<SearchLocation> {
        return flow {
            emit(searchLocationService.getLocationDetails(placeId, Locale.getDefault().toString()))
        }.map(searchLocationRemoteMapper::map)
    }
}