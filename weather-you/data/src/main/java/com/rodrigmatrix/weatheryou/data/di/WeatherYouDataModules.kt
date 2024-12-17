package com.rodrigmatrix.weatheryou.data.di

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rodrigmatrix.weatheryou.core.utils.UnitLocale
import com.rodrigmatrix.weatheryou.data.BuildConfig
import com.rodrigmatrix.weatheryou.data.analytics.WeatherYouAnalytics
import com.rodrigmatrix.weatheryou.data.analytics.WeatherYouAnalyticsImpl
import com.rodrigmatrix.weatheryou.data.local.*
import com.rodrigmatrix.weatheryou.data.local.database.LocationsDatabase
import com.rodrigmatrix.weatheryou.data.local.database.WeatherDatabase
import com.rodrigmatrix.weatheryou.data.local.database.WidgetDatabase
import com.rodrigmatrix.weatheryou.data.mapper.*
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSource
import com.rodrigmatrix.weatheryou.data.remote.RemoteConfigDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.builder.RetrofitClientGenerator
import com.rodrigmatrix.weatheryou.data.remote.interceptor.GoogleMapsInterceptor
import com.rodrigmatrix.weatheryou.data.remote.interceptor.LocationIqInterceptor
import com.rodrigmatrix.weatheryou.data.remote.interceptor.NinjasApiInterceptor
import com.rodrigmatrix.weatheryou.data.remote.interceptor.OpenWeatherInterceptor
import com.rodrigmatrix.weatheryou.data.remote.interceptor.VisualCrossingInterceptor
import com.rodrigmatrix.weatheryou.data.remote.interceptor.WeatherKitInterceptor
import com.rodrigmatrix.weatheryou.data.remote.openweather.OpenWeatherRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.remoteconfig.WeatherYouRemoteConfigKeys.LOCATION_IQ_API_KEY
import com.rodrigmatrix.weatheryou.data.remote.remoteconfig.WeatherYouRemoteConfigKeys.NINJAS_API_KEY
import com.rodrigmatrix.weatheryou.data.remote.remoteconfig.WeatherYouRemoteConfigKeys.OPEN_WEATHER_API_KEY
import com.rodrigmatrix.weatheryou.data.remote.remoteconfig.WeatherYouRemoteConfigKeys.VISUAL_CROSSING_API_KEY
import com.rodrigmatrix.weatheryou.data.remote.remoteconfig.WeatherYouRemoteConfigKeys.WEATHER_PROVIDER
import com.rodrigmatrix.weatheryou.data.remote.search.SearchLocalDataSource
import com.rodrigmatrix.weatheryou.data.remote.search.SearchLocalDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSource
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.visualcrossing.VisualCrossingRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.weatherkit.WeatherKitRemoteDataSourceImpl
import com.rodrigmatrix.weatheryou.data.remote.weatherkit.jwt.WeatherKitTokenGenerator
import com.rodrigmatrix.weatheryou.data.repository.RemoteConfigRepositoryImpl
import com.rodrigmatrix.weatheryou.data.repository.SearchRepositoryImpl
import com.rodrigmatrix.weatheryou.data.repository.SettingsRepositoryImpl
import com.rodrigmatrix.weatheryou.data.repository.WeatherRepositoryImpl
import com.rodrigmatrix.weatheryou.data.service.ApiNinjasService
import com.rodrigmatrix.weatheryou.data.service.LocationIqService
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.data.service.SearchLocationService
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.data.service.WeatherKitService
import com.rodrigmatrix.weatheryou.domain.repository.RemoteConfigRepository
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.usecase.*
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

object WeatherYouDataModules {

    private const val GOOGLE_MAPS_SERVICE = "GOOGLE_MAPS_SERVICE"
    private const val WEATHER_YOU_SHARED_PREFERENCES = "weather_you_shared_preferences"
    private const val OPEN_WEATHER = "OPEN_WEATHER"
    private const val API_NINJAS = "API_NINJAS"
    private const val WEATHER_KIT = "WEATHER_KIT"
    private const val LOCATION_IQ = "LOCATION_IQ"

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
        factory {
            AddLocationUseCase(
                weatherRepository = get(),
                getRemoteConfigLongUseCase = get(),
                settingsRepository = get(),
            )
        }
        factory { DeleteLocationUseCase(weatherRepository = get()) }
        factory { UpdateLocationsUseCase(weatherRepository = get()) }
        factory { FetchLocationUseCase(weatherRepository = get()) }
        factory { GetRemoteConfigLongUseCase(remoteConfigRepository = get()) }
        factory { GetWidgetTemperatureUseCase(weatherRepository = get()) }
        factory { SetWidgetLocationUseCase(weatherRepository = get()) }
        factory { DeleteWidgetLocationUseCase(weatherRepository = get()) }
        factory { GetLocationSizeUseCase(weatherRepository = get()) }
        factory { GetLocationsUseCase(weatherRepository = get()) }
        factory { UpdateLocationsListOrderUseCase(weatherRepository = get()) }
        factory { UpdateWidgetLocationsUseCase(weatherRepository = get()) }
        factory { GetLocationByLatLongUseCase(weatherRepository = get()) }
    }

    private val repositoryModule = module {
        factory<WeatherRepository> {
            WeatherRepositoryImpl(
                weatherYouRemoteDataSource = get(),
                weatherLocalDataSource = get(),
                userLocationDataSource = get(),
                searchRepository = get(),
                applicationContext = androidApplication(),
            )
        }
        factory<SearchRepository> {
            SearchRepositoryImpl(
                searchLocalDataSource = get(),
                searchRemoteDataSource = get(),
                famousCitiesMapper = FamousCitiesMapper(),
            )
        }
        factory<SettingsRepository> { SettingsRepositoryImpl(settingsLocalDataSource = get()) }
        factory<RemoteConfigRepository> {
            RemoteConfigRepositoryImpl(remoteConfigDataSource = get())
        }
    }

    @SuppressLint("MissingPermission")
    private val dataSourceModule = module {
        single<WeatherYouRemoteDataSource> {
            //val provider = get<RemoteConfigDataSource>().getString(WEATHER_PROVIDER)
            WeatherKitRemoteDataSourceImpl(
                weatherKitService = get(),
                weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
            )
//            when (provider) {
//                OPEN_WEATHER -> {
//                    OpenWeatherRemoteDataSourceImpl(
//                        openWeatherService = get(),
//                        OpenWeatherRemoteMapper(OpenWeatherConditionMapper())
//                    )
//                }
//                WEATHER_KIT -> {
//                    WeatherKitRemoteDataSourceImpl(
//                        weatherKitService = get(),
//                        weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
//                    )
//                }
//                else -> {
//                    VisualCrossingRemoteDataSourceImpl(
//                        visualCrossingService = get(),
//                        VisualCrossingRemoteMapper(VisualCrossingWeatherConditionMapper())
//                    )
//                }
//            }
        }
        factory<WeatherLocalDataSource> {
            WeatherLocalDataSourceImpl(
                weatherDAO = get(),
                widgetDataDao = get(),
                locationsDAO = get(),
            )
        }
        factory<UserLocationDataSource> {
            UserLocationDataSourceImpl(
                get(),
                get()
            )
        }
        factory<RemoteConfigDataSource> {
            RemoteConfigDataSourceImpl(
                remoteConfig = Firebase.remoteConfig
            )
        }
        factory<SearchRemoteDataSource> {
            SearchRemoteDataSourceImpl(
                locationIqService = get(named(LOCATION_IQ)),
            )
        }
        factory<SearchLocalDataSource> {
            SearchLocalDataSourceImpl(context = androidContext())
        }
        single {
            PreferenceDataStoreFactory.create(
                migrations = listOf(
                    SharedPreferencesMigration(
                        context = androidApplication(),
                        sharedPreferencesName = WEATHER_YOU_SHARED_PREFERENCES,
                    ),
                )
            ) {
                androidApplication().preferencesDataStoreFile("weather_you_data_store")
            }
        }
        factory<SettingsLocalDataSource> {
            SettingsLocalDataSourceImpl(
                dataStore = get(),
                unitLocale = UnitLocale(Locale.getDefault()),
            )
        }
        factory<WeatherYouAnalytics> {
            WeatherYouAnalyticsImpl(
                firebaseAnalytics = FirebaseAnalytics.getInstance(androidApplication())
            )
        }
    }


    private val otherModules = module {
        single { WeatherDatabase(androidApplication()) }
        single { LocationsDatabase(androidApplication()) }
        single { WidgetDatabase(androidApplication()) }
        factory { get<LocationsDatabase>().locationsDao() }
        factory { get<WidgetDatabase>().widgetDataDao() }
        factory { get<WeatherDatabase>().weatherDao() }
        factory {
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
        single {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(
                    OpenWeatherInterceptor(
                        apiKey = get<RemoteConfigDataSource>().getString(OPEN_WEATHER_API_KEY)
                            .ifEmpty { BuildConfig.OPEN_WEATHER_TOKEN }
                    )
                )
            }.build()
            val openWeatherRetrofit = RetrofitClientGenerator().create(
                baseUrl = BuildConfig.OPEN_WEATHER_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            openWeatherRetrofit.create(OpenWeatherService::class.java)
        }
        single {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(
                    VisualCrossingInterceptor(
                        apiKey = get<RemoteConfigDataSource>().getString(VISUAL_CROSSING_API_KEY)
                            .ifEmpty { BuildConfig.VISUAL_CODING_TOKEN }
                    )
                )
            }.build()
            val visualCrossingRetrofit = RetrofitClientGenerator().create(
                baseUrl = BuildConfig.VISUAL_CODING_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            visualCrossingRetrofit.create(VisualCrossingService::class.java)
        }
        single {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(
                    WeatherKitInterceptor(
                        weatherKitTokenGenerator = WeatherKitTokenGenerator()
                    )
                )
            }.build()
            val weatherKitRetrofit = RetrofitClientGenerator().create(
                baseUrl = BuildConfig.WEATHER_KIT_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            weatherKitRetrofit.create(WeatherKitService::class.java)
        }
        single(named(API_NINJAS)) {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(
                    NinjasApiInterceptor(
                        apiKey = get<RemoteConfigDataSource>().getString(NINJAS_API_KEY)
                            .ifEmpty { BuildConfig.API_NINJAS_TOKEN }
                    )
                )
            }.build()
            val ninjasRetrofit = RetrofitClientGenerator().create(
                baseUrl = BuildConfig.API_NINJAS_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            ninjasRetrofit.create(ApiNinjasService::class.java)
        }
        single(named(LOCATION_IQ)) {
            val interceptor = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    addNetworkInterceptor(logging)
                }
                addNetworkInterceptor(
                    LocationIqInterceptor(
                        apiKey = get<RemoteConfigDataSource>().getString(LOCATION_IQ_API_KEY)
                            .ifEmpty { BuildConfig.LOCATION_IQ_TOKEN }
                    )
                )
            }.build()
            val locationIqRetrofit = RetrofitClientGenerator().create(
                baseUrl = BuildConfig.LOCATION_IQ_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            locationIqRetrofit.create(LocationIqService::class.java)
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
                baseUrl = BuildConfig.GOOGLE_MAPS_URL,
                converterFactory = get<Json>().asConverterFactory("application/json".toMediaType()),
                httpClient = interceptor
            )
            searchRetrofit.create(SearchLocationService::class.java)
        }
        single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
        factory { Geocoder(androidApplication(), Locale.getDefault()) }
    }

}