package com.rodrigmatrix.weatheryou.components.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@SuppressLint("NewApi")
@Composable
fun WeatherYouTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= 31,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context).toWeatherYouColors()
            } else {
                dynamicLightColorScheme(context).toWeatherYouColors()
            }
        }
        darkTheme -> DarkColorScheme.toWeatherYouColors()
        else -> LightColorScheme.toWeatherYouColors()
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
            typography = Typography,
            content = content
        )
    }

}

object WeatherYouTheme {

    val colorScheme: WeatherYouColors
        @Composable
        get() = WeatherYouColorScheme.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}