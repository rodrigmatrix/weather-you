package com.rodrigmatrix.weatheryou.data.remote

interface RemoteConfigDataSource {

   fun getString(key: String): String

   fun getLong(key: String): Long

   fun getBoolean(key: String): Boolean
}