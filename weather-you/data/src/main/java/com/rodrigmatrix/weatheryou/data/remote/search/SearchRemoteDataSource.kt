package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import kotlinx.coroutines.flow.Flow

interface SearchRemoteDataSource {

    fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>>
}
