package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUseCase(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(placeId: String): Flow<SearchLocation> {
        return searchRepository.getLocation(placeId)
    }
}