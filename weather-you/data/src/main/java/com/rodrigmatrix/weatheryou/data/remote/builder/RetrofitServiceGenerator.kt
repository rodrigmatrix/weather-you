package com.rodrigmatrix.weatheryou.data.remote.builder

import retrofit2.Retrofit

internal class RetrofitServiceGenerator(private val retrofit: Retrofit) {

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}