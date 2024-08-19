package com.rodrigmatrix.weatheryou.tv.presentation.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.LocalWeatherYouColorMode
import com.rodrigmatrix.weatheryou.components.theme.LocalWeatherYouColorScheme
import com.rodrigmatrix.weatheryou.components.theme.LocalWeatherYouThemeMode
import com.rodrigmatrix.weatheryou.components.theme.LocalWeatherYouTypography
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouColors
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTypography

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

@Composable
fun WeatherYouTvTheme(
    themeMode: ThemeMode = ThemeMode.Dark,
    colorMode: ColorMode = ColorMode.Default,
    content: @Composable () -> Unit
) {
    val darkTheme = themeMode in listOf(ThemeMode.Dark, ThemeMode.System)
    val colorScheme = when (colorMode) {
        ColorMode.Dynamic -> when {
            darkTheme -> TvDarkColorScheme.switch()
            else -> TvLightColorScheme
        }
        ColorMode.Default -> when {
            darkTheme -> TvDarkColorScheme.switch()
            else -> TvLightColorScheme.switch()
        }
        ColorMode.Mosque -> when {
            darkTheme -> mosqueDarkScheme.switch()
            else -> mosqueLightScheme.switch()
        }
        ColorMode.DarkFern -> when {
            darkTheme -> darkFernDarkScheme.switch()
            else -> darkFernLightScheme.switch()
        }
        ColorMode.FreshEggplant -> when {
            darkTheme -> freshEggplantDarkScheme.switch()
            else -> freshEggplantLightScheme.switch()
        }
        ColorMode.Carmine -> when {
            darkTheme -> carmineDarkScheme.switch()
            else -> carmineLightScheme.switch()
        }
        ColorMode.Cinnamon -> when {
            darkTheme -> cinnamonDarkScheme.switch()
            else -> cinnamonLightScheme.switch()
        }
        ColorMode.PeruTan -> when {
            darkTheme -> peruTanDarkScheme.switch()
            else -> peruTanLightScheme.switch()
        }
        ColorMode.Gigas -> when {
            darkTheme -> gigasDarkScheme.switch()
            else -> gigasLightScheme.switch()
        }
    }
    val typography = WeatherYouTypography(
        displayLarge = MaterialTheme.typography.displayLarge,
        displayMedium = MaterialTheme.typography.displayMedium,
        displaySmall = MaterialTheme.typography.displaySmall,
        headlineLarge = MaterialTheme.typography.headlineLarge,
        headlineMedium = MaterialTheme.typography.headlineMedium,
        headlineSmall = MaterialTheme.typography.headlineSmall,
        titleLarge = MaterialTheme.typography.titleLarge,
        titleMedium = MaterialTheme.typography.titleMedium,
        titleSmall = MaterialTheme.typography.titleSmall,
        bodyLarge = MaterialTheme.typography.bodyLarge,
        bodyMedium = MaterialTheme.typography.bodyMedium,
        bodySmall = MaterialTheme.typography.bodySmall,
        labelLarge = MaterialTheme.typography.labelLarge,
        labelMedium = MaterialTheme.typography.labelMedium,
        labelSmall = MaterialTheme.typography.labelSmall,
    )
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
    CompositionLocalProvider(
        LocalWeatherYouColorScheme provides colorScheme.toWeatherYouColors(),
        LocalWeatherYouTypography provides typography,
        LocalWeatherYouThemeMode provides themeMode,
        LocalWeatherYouColorMode provides colorMode,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}


@Composable
private fun animateColor(targetValue: Color) =
    animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 2000),
    ).value

@Composable
fun ColorScheme.switch(): ColorScheme = copy(
    primary = animateColor(primary),
    onPrimary = animateColor(onPrimary),
    primaryContainer = animateColor(primaryContainer),
    onPrimaryContainer = animateColor(onPrimaryContainer),
    secondary = animateColor(secondary),
    onSecondary = animateColor(onSecondary),
    secondaryContainer = animateColor(secondaryContainer),
    onSecondaryContainer = animateColor(onSecondaryContainer),
    tertiary = animateColor(tertiary),
    onTertiary = animateColor(onTertiary),
    tertiaryContainer = animateColor(tertiaryContainer),
    onTertiaryContainer = animateColor(onTertiaryContainer),
    background = animateColor(background),
    onBackground = animateColor(onBackground),
    surface = animateColor(surface),
    onSurface = animateColor(onSurface),
    surfaceVariant = animateColor(surfaceVariant),
    onSurfaceVariant = animateColor(onSurfaceVariant),
    surfaceTint = animateColor(surfaceTint),
    inverseSurface = animateColor(inverseSurface),
    inverseOnSurface = animateColor(inverseOnSurface),
    error = animateColor(error),
    onError = animateColor(onError),
    errorContainer = animateColor(errorContainer),
    onErrorContainer = animateColor(onErrorContainer),
)