package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.RemoteConfigRepository

class GetRemoteConfigLongUseCase(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    operator fun invoke(key: String): Long {
        return remoteConfigRepository.getLong(key)
    }
}