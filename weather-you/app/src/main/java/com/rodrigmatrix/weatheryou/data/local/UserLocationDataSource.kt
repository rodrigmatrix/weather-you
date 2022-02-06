package com.rodrigmatrix.weatheryou.data.local

import kotlinx.coroutines.flow.Flow

interface UserLocationDataSource {

    fun getCurrentLocation(): Flow<String>
}