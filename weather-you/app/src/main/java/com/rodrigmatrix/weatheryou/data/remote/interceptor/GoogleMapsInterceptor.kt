package com.rodrigmatrix.weatheryou.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "key"

class GoogleMapsInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder().apply {
            addQueryParameter(API_KEY, apiKey)
        }.build()
        val request = originalRequest.newBuilder().apply {
            url(url)
        }.build()

        return chain.proceed(request)
    }
}