package com.rodrigmatrix.weatheryou.data.local

import kotlinx.coroutines.flow.Flow

interface SharedPreferencesDataSource {

    fun getString(key: String, defaultValue: String? = null): Flow<String>

    fun setString(key: String, value: String): Flow<Unit>
}