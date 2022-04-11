package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSource
import com.rodrigmatrix.weatheryou.domain.repository.RemoteConfigRepository

class RemoteConfigRepositoryImpl(
    private val remoteConfigDataSource: RemoteConfigDataSource
) : RemoteConfigRepository {

    override fun getString(key: String): String {
        return remoteConfigDataSource.getString(key)
    }

    override fun getLong(key: String): Long {
        return remoteConfigDataSource.getLong(key)
    }
}