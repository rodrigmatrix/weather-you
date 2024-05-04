package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface SearchLocalDataSource {

    fun searchLocation(name: String): Flow<List<Location>>
}