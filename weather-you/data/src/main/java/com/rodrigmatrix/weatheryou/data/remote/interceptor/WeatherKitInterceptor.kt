package com.rodrigmatrix.weatheryou.data.remote.interceptor

import com.rodrigmatrix.weatheryou.data.remote.weatherkit.jwt.WeatherKitTokenGenerator
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class WeatherKitInterceptor(
    private val weatherKitTokenGenerator: WeatherKitTokenGenerator,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = try {
            weatherKitTokenGenerator.generateToken()
        } catch (exception: Exception) {
            // OkHttp reports IOException through Retrofit's normal failure callback. Letting
            // key/configuration exceptions escape the dispatcher can terminate the app process.
            throw IOException("Unable to authorize WeatherKit request", exception)
        }
        val originalRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(originalRequest)
    }
}
