package com.rodrigmatrix.weatheryou.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigDataSourceImpl(
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigDataSource {

    override fun getString(key: String): String {
        return remoteConfig.getString(key)
    }
}