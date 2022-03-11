package com.rodrigmatrix.weatheryou.data.di

import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rodrigmatrix.weatheryou.BuildConfig
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSourceImpl
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSourceImpl
import com.rodrigmatrix.weatheryou.data.local.database.WeatherDatabase
import com.rodrigmatrix.weatheryou.data.mapper.*
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSource
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.visualcrossing.VisualCrossingRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitClientGenerator
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitServiceGenerator
import com.rodrigmatrix.weatheryou.data.remote.openweather.OpenWeatherRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.repository.SearchRepositoryImpl
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.data.repository.WeatherRepositoryImpl
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

val dataModule: List<Module>
    get() = useCaseModule + repositoryModule + dataSourceModule + otherModules

private val useCaseModule = module {
    factory { GetFamousLocationsUseCase(get()) }
}

private val repositoryModule = module {
    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherYouRemoteDataSource = get(),
            weatherLocalDataSource = get(),
            weatherLocationDomainToEntityMapper = WeatherLocationDomainToEntityMapper()
        )
    }
    factory<SearchRepository> { SearchRepositoryImpl(get(), FamousCitiesMapper()) }
}

private val dataSourceModule = module {
    factory<WeatherYouRemoteDataSource> {
        VisualCrossingRemoteDataSourceImpl(
            visualCrossingService = get(),
            VisualCrossingRemoteMapper(VisualCrossingWeatherIconMapper())
        )
    }
//    factory<WeatherYouRemoteDataSource> {
//        OpenWeatherRemoteDataSourceImpl(
//            openWeatherService = get(),
//            OpenWeatherRemoteMapper(OpenWeatherIconMapper())
//        )
//    }
    factory<WeatherLocalDataSource> { WeatherLocalDataSourceImpl(get(), get()) }
    factory<UserLocationDataSource> { UserLocationDataSourceImpl(get(), get()) }
    factory<RemoteConfigDataSource> { RemoteConfigDataSourceImpl(get()) }
}

@OptIn(ExperimentalSerializationApi::class)
private val otherModules = module {
    single { WeatherDatabase(androidApplication()) }
    factory { get<WeatherDatabase>().locationsDao() }
    factory { Gson() }
    single {
        Firebase.remoteConfig
    }
    factory {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                addNetworkInterceptor(logging)
            }
        }.build()
    }
    single {
        val openWeatherRetrofit = RetrofitClientGenerator().create(
            baseUrl = BuildConfig.OPEN_WEATHER_URL,
            converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
            httpClient = get()
        )
        openWeatherRetrofit.create(OpenWeatherService::class.java)
    }
    single {
        val visualCrossingRetrofit = RetrofitClientGenerator().create(
            baseUrl =
            BuildConfig.VISUAL_CODING_URL,
            converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
            httpClient = get()
        )
        visualCrossingRetrofit.create(VisualCrossingService::class.java)
    }
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory { Geocoder(androidContext(), Locale.getDefault()) }
}