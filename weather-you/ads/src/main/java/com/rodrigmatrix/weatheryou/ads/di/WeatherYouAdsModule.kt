package com.rodrigmatrix.weatheryou.ads.di

import com.rodrigmatrix.weatheryou.ads.manager.AdsManager
import com.rodrigmatrix.weatheryou.ads.manager.AdsManagerImpl
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

object WeatherYouAdsModule {

    fun loadModules() {
        loadKoinModules(listOf(adsModule))
    }

    private val adsModule = module {
        factory<AdsManager> {
            AdsManagerImpl(
                remoteConfigRepository = get(),
            )
        }
    }
}