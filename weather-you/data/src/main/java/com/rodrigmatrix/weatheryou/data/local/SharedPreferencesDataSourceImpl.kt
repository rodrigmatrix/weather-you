package com.rodrigmatrix.weatheryou.data.local

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {

    override fun getString(key: String, defaultValue: String?): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(key, defaultValue).orEmpty())
        }
    }

    override fun setString(key: String, value: String): Flow<Unit> {
        return flow {
            emit(sharedPreferences.edit().putString(key, value).apply())
        }
    }
}