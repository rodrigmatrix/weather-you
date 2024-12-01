package com.rodrigmatrix.weatheryou.home.di

import com.rodrigmatrix.weatheryou.home.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object HomeModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel {
            HomeViewModel(
                updateLocationsUseCase = get(),
                deleteLocationUseCase = get(),
                getAppSettingsUseCase = get(),
                getLocationsUseCase = get(),
                updateLocationsListOrderUseCase = get(),
            )
        }
    }
}