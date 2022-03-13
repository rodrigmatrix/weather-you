package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class GetFamousLocationsUseCase(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(): Flow<List<City>> {
        return searchRepository.getFamousCities()
    }
}