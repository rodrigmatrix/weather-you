package com.rodrigmatrix.weatheryou.data.remote.builder

import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitClientGenerator {

    fun create(
        baseUrl: String,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }
}