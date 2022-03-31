package com.rodrigmatrix.weatheryou.data.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rodrigmatrix.weatheryou.data.BuildConfig
import com.rodrigmatrix.weatheryou.data.local.*
import com.rodrigmatrix.weatheryou.data.local.database.WeatherDatabase
import com.rodrigmatrix.weatheryou.data.mapper.*
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSource
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitClientGenerator
import com.rodrigmatrix.weatheryou.data.remote.interceptor.GoogleMapsInterceptor
import com.rodrigmatrix.weatheryou.data.remote.openweather.OpenWeatherRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.visualcrossing.VisualCrossingRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.repository.SearchRepositoryImpl
import com.rodrigmatrix.weatheryou.data.repository.SettingsRepositoryImpl
import com.rodrigmatrix.weatheryou.data.repository.WeatherRepositoryImpl
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.data.service.SearchLocationService
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.usecase.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
object WeatherYouDataModules {

    private const val GOOGLE_MAPS_SERVICE = "GOOGLE_MAPS_SERVICE"
    private const val WEATHER_YOU_SHARED_PREFERENCES = "weather_you_shared_preferences"

    fun loadModules() {
        loadKoinModules(
            listOf(
                useCaseModule,
                repositoryModule,
                dataSourceModule,
                otherModules
            )
        )
    }

    private val useCaseModule = module {
        factory { GetFamousLocationsUseCase(searchRepository = get()) }
        factory { SearchLocationUseCase(searchRepository = get()) }
        factory { GetLocationUseCase(searchRepository = get()) }
        factory { AddLocationUseCase(weatherRepository = get()) }
        factory { DeleteLocationUseCase(weatherRepository = get()) }
        factory { FetchLocationsUseCase(weatherRepository = get()) }
        factory { FetchLocationUseCase(weatherRepository = get()) }
    }

    private val repositoryModule = module {
        factory<WeatherRepository> {
            WeatherRepositoryImpl(
                weatherYouRemoteDataSource = get(),
                weatherLocalDataSource = get(),
                settingsRepository = get(),
                weatherLocationDomainToEntityMapper = WeatherLocationDomainToEntityMapper()
            )
        }
        factory<SearchRepository> {
            SearchRepositoryImpl(
                searchRemoteDataSource = get(),
                famousCitiesMapper = FamousCitiesMapper()
            )
        }
        factory<SettingsRepository> { SettingsRepositoryImpl(settingsLocalDataSource = get()) }
    }

    private val dataSourceModule = module {
        factory<WeatherYouRemoteDataSource> {
            if (true) {
                OpenWeatherRemoteDataSourceImpl(
                    openWeatherService = get(),
                    OpenWeatherRemoteMapper(OpenWeatherIconMapper())
                )
            } else {
                VisualCrossingRemoteDataSourceImpl(
                    visualCrossingService = get(),
                    VisualCrossingRemoteMapper(VisualCrossingWeatherIconMapper())
                )
            }
        }
        factory<WeatherLocalDataSource> {
            WeatherLocalDataSourceImpl(
                weatherDAO = get(),
                userLocationDataSource = get(),
                currentLocationToEntityMapper = CurrentLocationToEntityMapper()
            )
        }
        factory<UserLocationDataSource> {
            UserLocationDataSourceImpl(
                get(),
                get()
            )
        }
        factory<RemoteConfigDataSource> { RemoteConfigDataSourceImpl(get()) }
        factory<SearchRemoteDataSource> {
            SearchRemoteDataSourceImpl(
                searchLocationService = get(named(GOOGLE_MAPS_SERVICE)),
                searchAutocompleteRemoteMapper = SearchAutocompleteRemoteMapper(),
                searchLocationRemoteMapper = SearchLocationRemoteMapper()
            )
        }
        factory<SharedPreferencesDataSource> {
            SharedPreferencesDataSourceImpl(
                sharedPreferences = androidApplication()
                    .getSharedPreferences(WEATHER_YOU_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            )
        }
        factory<SettingsLocalDataSource> {
            SettingsLocalDataSourceImpl(sharedPreferencesDataSource = get())
        }
    }


    private val otherModules = module {
        single { WeatherDatabase(androidApplication()) }
        factory { get<WeatherDatabase>().locationsDao() }
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
        single(named(GOOGLE_MAPS_SERVICE)) {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(GoogleMapsInterceptor(BuildConfig.GOOGLE_MAPS_TOKEN))
            }.build()
            val searchRetrofit = RetrofitClientGenerator().create(
                baseUrl =
                BuildConfig.GOOGLE_MAPS_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            searchRetrofit.create(SearchLocationService::class.java)
        }
        single { LocationServices.getFusedLocationProviderClient(androidContext()) }
        factory { Geocoder(androidContext(), Locale.getDefault()) }
    }

}