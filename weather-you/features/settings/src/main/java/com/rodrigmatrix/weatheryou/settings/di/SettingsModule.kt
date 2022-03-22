package com.rodrigmatrix.weatheryou.settings.di

import com.rodrigmatrix.weatheryou.domain.usecase.GetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object SettingsModule {

    fun loadModules() {
        loadKoinModules(presentationModule + domainModule)
    }

    private val domainModule = module {
        factory { GetTemperaturePreferenceUseCase(settingsRepository = get()) }
        factory { SetTemperaturePreferenceUseCase(settingsRepository = get()) }
    }

    private val presentationModule = module {
        viewModel {
            SettingsViewModel(
                getTemperaturePreferenceUseCase = get(),
                setTemperaturePreferenceUseCase = get()
            )
        }
    }
}