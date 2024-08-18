package com.rodrigmatrix.weatheryou.components.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@SuppressLint("NewApi")
@Composable
fun WeatherYouTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= 31,
    colorMode: ColorMode = ColorMode.Dynamic,
    content: @Composable () -> Unit
) {
    val themeMode = if (darkTheme) {
        ThemeMode.Dark
    } else {
        ThemeMode.Light
    }
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (themeMode == ThemeMode.Dark) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        colorMode == ColorMode.Mosque -> when {
            darkTheme -> mosqueDarkScheme
            else -> mosqueLightScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
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