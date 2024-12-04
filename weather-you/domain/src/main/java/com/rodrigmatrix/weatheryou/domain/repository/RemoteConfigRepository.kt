package com.rodrigmatrix.weatheryou.domain.repository

interface RemoteConfigRepository {

    fun getString(key: String): String

    fun getLong(key: String): Long

    fun getBoolean(key: String): Boolean
}