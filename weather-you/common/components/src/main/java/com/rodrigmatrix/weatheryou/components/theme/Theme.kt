package com.rodrigmatrix.weatheryou.components.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun WeatherYouTheme(
    themeMode: ThemeMode = ThemeMode.System,
    colorMode: ColorMode = ColorMode.Dynamic,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val context = LocalContext.current
    val colorScheme = when (colorMode) {
        ColorMode.Dynamic -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                darkTheme -> dynamicDarkColorScheme(context)
                else -> dynamicLightColorScheme(context)
            }
        } else {
            when {
                darkTheme -> DarkColorScheme.switch()
                else -> LightColorScheme.switch()
            }
        }
        ColorMode.Default -> when {
            darkTheme -> DarkColorScheme.switch()
            else -> LightColorScheme.switch()
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

enum class ThemeMode {
    System,
    Light,
    Dark,
}

enum class ColorMode {
    Dynamic,
    Default,
    Mosque,
    DarkFern,
    FreshEggplant,
    Carmine,
    Cinnamon,
    PeruTan,
    Gigas,
}

val LocalWeatherYouThemeMode = compositionLocalOf {
    ThemeMode.Light
}

val LocalWeatherYouColorMode = compositionLocalOf {
    ColorMode.Default
}

object WeatherYouTheme {

    val colorScheme: WeatherYouColors
        @Composable
        get() = LocalWeatherYouColorScheme.current

    val typography: WeatherYouTypography
        @Composable
        get() = LocalWeatherYouTypography.current

    val themeMode: ThemeMode
        @Composable
        get() = LocalWeatherYouThemeMode.current

    val colorMode: ColorMode
        @Composable
        get() = LocalWeatherYouColorMode.current

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
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
    outline = animateColor(outline),
    outlineVariant = animateColor(outlineVariant),
    scrim = animateColor(scrim),
    inversePrimary = animateColor(inversePrimary),
    surfaceBright = animateColor(surfaceBright),
    surfaceDim = animateColor(surfaceDim),
    surfaceContainer = animateColor(surfaceContainer),
    surfaceContainerHigh = animateColor(surfaceContainerHigh),
    surfaceContainerHighest = animateColor(surfaceContainerHighest),
    surfaceContainerLow = animateColor(surfaceContainerLow),
    surfaceContainerLowest = animateColor(surfaceContainerLowest),
)