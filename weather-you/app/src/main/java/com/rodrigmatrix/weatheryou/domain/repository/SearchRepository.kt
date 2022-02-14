package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.City
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getFamousCities(): Flow<List<City>>
}