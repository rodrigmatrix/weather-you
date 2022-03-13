package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import kotlinx.coroutines.flow.Flow

interface UserLocationDataSource {

    fun getCurrentLocation(): Flow<CurrentLocation>
}