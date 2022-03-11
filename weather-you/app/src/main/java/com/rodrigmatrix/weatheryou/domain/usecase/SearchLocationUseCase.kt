package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchLocationUseCase(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(query: String): Flow<List<SearchAutocompleteLocation>> {
        return searchRepository.searchLocation(query)
    }
}