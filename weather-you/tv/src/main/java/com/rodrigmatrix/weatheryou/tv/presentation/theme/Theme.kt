package com.rodrigmatrix.weatheryou.tv.presentation.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouColorScheme
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouColors

private val TvDarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
)

fun ColorScheme.toWeatherYouColors() = WeatherYouColors(
    primary = this.primary,
    onPrimary = this.onPrimary,
    primaryContainer = this.primaryContainer,
    onPrimaryContainer = this.onPrimaryContainer,
    inversePrimary = this.inversePrimary,
    secondary = this.secondary,
    onSecondaryContainer = this.onSecondaryContainer,
    onSecondary = this.onSecondary,
    secondaryContainer = this.secondaryContainer,
    tertiary = this.tertiary,
    onTertiary = this.onTertiary,
    tertiaryContainer = this.tertiaryContainer,
    onTertiaryContainer = this.onTertiary,
    background = this.background,
    onBackground = this.onBackground,
    surface = this.surface,
    onSurface = this.onSurface,
    surfaceVariant = this.surfaceVariant,
    onSurfaceVariant = this.onSurfaceVariant,
    surfaceTint = this.surfaceTint,
    inverseSurface = this.inverseSurface,
    inverseOnSurface = this.inverseOnSurface,
    error = this.error,
    onError = this.onError,
    errorContainer = this.errorContainer,
    onErrorContainer = this.onErrorContainer,
    outline = Color.Unspecified,
    outlineVariant = Color.Unspecified,
    scrim = this.scrim,
    surfaceBright = Color.Unspecified,
    surfaceDim = Color.Unspecified,
    surfaceContainer = Color.Unspecified,
    surfaceContainerHigh = Color.Unspecified,
    surfaceContainerHighest = Color.Unspecified,
    surfaceContainerLow = Color.Unspecified,
    surfaceContainerLowest = Color.Unspecified,
)


@SuppressLint("NewApi")
@Composable
fun WeatherYouTvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> TvDarkColorScheme.toWeatherYouColors()
        else -> TvDarkColorScheme.toWeatherYouColors()
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        val currentWindow = (view.context as? Activity)?.window
        SideEffect {
            currentWindow?.let {
                currentWindow.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                    darkTheme
            }
        }
    }
    CompositionLocalProvider(WeatherYouColorScheme provides colorScheme) {
        MaterialTheme(
            content = content
        )
    }
}