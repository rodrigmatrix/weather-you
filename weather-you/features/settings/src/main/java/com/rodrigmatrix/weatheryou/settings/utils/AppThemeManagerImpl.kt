package com.rodrigmatrix.weatheryou.settings.utils

import androidx.appcompat.app.AppCompatDelegate.*
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AppThemeManagerImpl(
    private val getAppThemePreferenceUseCase: GetAppThemePreferenceUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AppThemeManager {

    override suspend fun setAppTheme() {
        withContext(coroutineDispatcher) {
            when (getAppThemePreferenceUseCase().firstOrNull()) {
                AppThemePreference.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
                AppThemePreference.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
                else -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}