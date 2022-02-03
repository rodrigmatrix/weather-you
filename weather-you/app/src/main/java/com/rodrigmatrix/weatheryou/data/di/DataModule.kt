package com.rodrigmatrix.weatheryou.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rodrigmatrix.weatheryou.BuildConfig
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSourceImpl
import com.rodrigmatrix.weatheryou.data.local.database.WeatherDatabase
import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingLocalMapper
import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.mapper.WeatherIconMapper
import com.rodrigmatrix.weatheryou.data.remote.VisualCrossingRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.VisualCrossingRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitClientGenerator
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitServiceGenerator
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.data.repository.WeatherRepositoryImpl
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule: List<Module>
    get() = useCaseModule + repositoryModule + dataSourceModule + otherModules

private val useCaseModule = module {

}

private val repositoryModule = module {
    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            visualCrossingRemoteDataSource = get(),
            weatherLocalDataSource = get(),
            visualCrossingRemoteMapper = VisualCrossingRemoteMapper(
                weatherIconMapper = WeatherIconMapper()
            ),
            visualCrossingLocalMapper = VisualCrossingLocalMapper()
        )
    }
}

private val dataSourceModule = module {
    factory<VisualCrossingRemoteDataSource> { VisualCrossingRemoteDataSourceImpl(get()) }
    factory<WeatherLocalDataSource> { WeatherLocalDataSourceImpl(get()) }
}

@OptIn(ExperimentalSerializationApi::class)
private val otherModules = module {
    single { WeatherDatabase(androidApplication()) }
    factory { get<WeatherDatabase>().locationsDao() }
    factory {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single {
        RetrofitClientGenerator().create(
            baseUrl =
            BuildConfig.VISUAL_CODING_URL,
            converterFactory = get<Json>().asConverterFactory("application/json".toMediaType())
        )
    }
    single { RetrofitServiceGenerator(retrofit = get()) }
    factory { get<RetrofitServiceGenerator>().createService(VisualCrossingService::class.java) }
}