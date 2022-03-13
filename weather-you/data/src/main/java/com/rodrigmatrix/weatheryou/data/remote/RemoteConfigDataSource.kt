package com.rodrigmatrix.weatheryou.data.remote

interface RemoteConfigDataSource {

   fun getString(key: String): String
}