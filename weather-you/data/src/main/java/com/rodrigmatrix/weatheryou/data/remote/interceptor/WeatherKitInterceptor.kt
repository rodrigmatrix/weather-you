package com.rodrigmatrix.weatheryou.data.remote.interceptor

import com.rodrigmatrix.weatheryou.data.remote.weatherkit.jwt.WeatherKitTokenGenerator
import okhttp3.Interceptor
import okhttp3.Response

class WeatherKitInterceptor(
    private val weatherKitTokenGenerator: WeatherKitTokenGenerator,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${weatherKitTokenGenerator.generateToken()}")
            .build()

        return chain.proceed(originalRequest)
    }
}