package com.rodrigmatrix.weatheryou.settings.di

import com.rodrigmatrix.weatheryou.domain.usecase.GetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetTemperaturePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManagerImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object SettingsModule {

    fun loadModules() {
        loadKoinModules(presentationModule + domainModule + otherModule)
    }

    private val domainModule = module {
        factory { GetTemperaturePreferenceUseCase(settingsRepository = get()) }
        factory { SetTemperaturePreferenceUseCase(settingsRepository = get()) }
        factory { GetAppThemePreferenceUseCase(settingsRepository = get()) }
        factory { SetAppThemePreferenceUseCase(settingsRepository = get()) }
        factory { GetAppColorPreferenceUseCase(settingsRepository = get()) }
        factory { SetAppColorPreferenceUseCase(settingsRepository = get()) }
    }

    private val presentationModule = module {
        viewModel {
            SettingsViewModel(
                getTemperaturePreferenceUseCase = get(),
                setTemperaturePreferenceUseCase = get(),
                getAppThemePreferenceUseCase = get(),
                setAppThemePreferenceUseCase = get(),
                getAppColorPreferenceUseCase = get(),
                setAppColorPreferenceUseCase = get(),
                appThemeManager = get()
            )
        }
    }

    private val otherModule = module {
        factory<AppThemeManager> { AppThemeManagerImpl(getAppThemePreferenceUseCase = get()) }
    }
}