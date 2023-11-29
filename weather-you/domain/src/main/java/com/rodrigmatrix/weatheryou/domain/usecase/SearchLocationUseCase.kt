package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchLocationUseCase(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(query: String): Flow<List<SearchAutocompleteLocation>> {
        if (query.length <= 3) {
            return flowOf(emptyList())
        }
        return searchRepository.searchLocation(query)
    }
}