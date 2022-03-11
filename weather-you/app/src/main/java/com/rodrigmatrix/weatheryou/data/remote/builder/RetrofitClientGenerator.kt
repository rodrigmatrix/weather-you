package com.rodrigmatrix.weatheryou.data.remote.builder

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitClientGenerator {

    fun create(
        baseUrl: String,
        converterFactory: Converter.Factory,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(httpClient)
            .build()
    }
}