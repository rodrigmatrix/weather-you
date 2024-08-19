package com.rodrigmatrix.weatheryou.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppColorPreferenceUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppThemePreferenceUseCase
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getAppColorPreferenceUseCase: GetAppColorPreferenceUseCase by inject()
    private val getAppThemePreferenceUseCase: GetAppThemePreferenceUseCase by inject()
    private val appThemeManager: AppThemeManager by inject()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var colorMode by remember { mutableStateOf(ColorMode.Default) }
            var themeMode by remember { mutableStateOf(ThemeMode.Dark) }
            LaunchedEffect(Unit) {
                getAppColorPreferenceUseCase().collect {
                    colorMode = when (it) {
                        AppColorPreference.DYNAMIC -> ColorMode.Dynamic
                        AppColorPreference.DEFAULT -> ColorMode.Default
                        AppColorPreference.MOSQUE -> ColorMode.Mosque
                        AppColorPreference.DARK_FERN -> ColorMode.DarkFern
                        AppColorPreference.FRESH_EGGPLANT -> ColorMode.FreshEggplant
                        AppColorPreference.CARMINE -> ColorMode.Carmine
                        AppColorPreference.CINNAMON -> ColorMode.Cinnamon
                        AppColorPreference.PERU_TAN -> ColorMode.PeruTan
                        AppColorPreference.GIGAS -> ColorMode.Gigas
                    }
                }
            }
            LaunchedEffect(Unit) {
                getAppThemePreferenceUseCase().collect {
                    themeMode = when (it) {
                        AppThemePreference.SYSTEM_DEFAULT -> ThemeMode.Dark
                        AppThemePreference.LIGHT -> ThemeMode.Light
                        AppThemePreference.DARK -> ThemeMode.Dark
                    }
                    appThemeManager.setAppTheme(enableFollowSystem = false)
                }
            }
            WeatherYouTheme(
                themeMode = themeMode,
                colorMode = colorMode,
            ) {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)

                HomeNavigationScreen(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures
                )
            }
        }
    }
}
