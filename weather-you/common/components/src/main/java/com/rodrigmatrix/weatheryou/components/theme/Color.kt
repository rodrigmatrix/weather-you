package com.rodrigmatrix.weatheryou.components.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme.themeSettings

val WeatherYouColors.rain: Color
    @Composable
    get() = if (WeatherYouTheme.isDarkTheme) {
        Color(0xFF54bef0)
    } else {
        Color(0xFF54bef0)
    }

val WeatherYouColors.snow: Color
    @Composable
    get() = if (WeatherYouTheme.isDarkTheme) {
        Color(0xffffffff)
    } else {
        Color(0xffffffff)
    }

val WeatherYouColors.weatherTextColor: Color
    @Composable
    get() = if (themeSettings.showWeatherAnimations) {
        Color.White
    } else {
        onSecondaryContainer
    }

val WeatherYouColors.backgroundDarkColor: Color
    @Composable
    get() = if (themeSettings.showWeatherAnimations) {
        Color.White
    } else {
        onSecondaryContainer
    }

val md_theme_light_primary = Color(0xFF4159a9)
val md_theme_light_onPrimary = Color(0xFFffffff)
val md_theme_light_primaryContainer = Color(0xFFdbe1ff)
val md_theme_light_onPrimaryContainer = Color(0xFF001552)
val md_theme_light_secondary = Color(0xFF3f5aa9)
val md_theme_light_onSecondary = Color(0xFFffffff)
val md_theme_light_secondaryContainer = Color(0xffbac7ff)
val md_theme_light_onSecondaryContainer = Color(0xFF00164f)
val md_theme_light_tertiary = Color(0xFF7D5260)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD8E4)
val md_theme_light_onTertiaryContainer = Color(0xFF31111D)
val md_theme_light_error = Color(0xFFB3261E)
val md_theme_light_errorContainer = Color(0xFFF9DEDC)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410E0B)
val md_theme_light_background = Color(0xFFFFFBFE)
val md_theme_light_onBackground = Color(0xFF1C1B1F)
val md_theme_light_surface = Color(0xFFFFFBFE)
val md_theme_light_onSurface = Color(0xFF1C1B1F)
val md_theme_light_surfaceVariant = Color(0xFFE7E0EC)
val md_theme_light_onSurfaceVariant = Color(0xFF49454F)
val md_theme_light_outline = Color(0xFF79747E)
val md_theme_light_inverseOnSurface = Color(0xFFF4EFF4)
val md_theme_light_inverseSurface = Color(0xFF313033)

val md_theme_dark_primary = Color(0xFFb4c4ff)
val md_theme_dark_onPrimary = Color(0xFF082978)
val md_theme_dark_primaryContainer = Color(0xFF274190)
val md_theme_dark_onPrimaryContainer = Color(0xFFdbe1ff)
val md_theme_dark_secondary = Color(0xFFb4c5ff)
val md_theme_dark_onSecondary = Color(0xFF022978)
val md_theme_dark_secondaryContainer = Color(0xFF424659)
val md_theme_dark_onSecondaryContainer = Color(0xFFdae1ff)
val md_theme_dark_tertiary = Color(0xFFEFB8C8)
val md_theme_dark_onTertiary = Color(0xFF492532)
val md_theme_dark_tertiaryContainer = Color(0xFF633B48)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD8E4)
val md_theme_dark_error = Color(0xFFF2B8B5)
val md_theme_dark_errorContainer = Color(0xFF8C1D18)
val md_theme_dark_onError = Color(0xFF601410)
val md_theme_dark_onErrorContainer = Color(0xFFF9DEDC)
val md_theme_dark_background = Color(0xFF1C1B1F)
val md_theme_dark_onBackground = Color(0xFFE6E1E5)
val md_theme_dark_surface = Color(0xFF1C1B1F)
val md_theme_dark_onSurface = Color(0xFFE6E1E5)
val md_theme_dark_surfaceVariant = Color(0xFF49454F)
val md_theme_dark_onSurfaceVariant = Color(0xFFCAC4D0)
val md_theme_dark_outline = Color(0xFF938F99)
val md_theme_dark_inverseOnSurface = Color(0xFF1C1B1F)
val md_theme_dark_inverseSurface = Color(0xFFE6E1E5)

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
)

val DarkColorScheme = darkColorScheme(
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
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
)

@Stable
data class WeatherYouColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
)

val LocalWeatherYouColorScheme = staticCompositionLocalOf {
    WeatherYouColors(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        primaryContainer = Color.Unspecified,
        onPrimaryContainer = Color.Unspecified,
        inversePrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondaryContainer = Color.Unspecified,
        onSecondary = Color.Unspecified,
        secondaryContainer = Color.Unspecified,
        tertiary = Color.Unspecified,
        onTertiary = Color.Unspecified,
        tertiaryContainer = Color.Unspecified,
        onTertiaryContainer = Color.Unspecified,
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        surfaceVariant = Color.Unspecified,
        onSurfaceVariant = Color.Unspecified,
        surfaceTint = Color.Unspecified,
        inverseSurface = Color.Unspecified,
        inverseOnSurface = Color.Unspecified,
        error = Color.Unspecified,
        onError = Color.Unspecified,
        errorContainer = Color.Unspecified,
        onErrorContainer = Color.Unspecified,
        outline = Color.Unspecified,
        outlineVariant = Color.Unspecified,
        scrim = Color.Unspecified,
        surfaceBright = Color.Unspecified,
        surfaceDim = Color.Unspecified,
        surfaceContainer = Color.Unspecified,
        surfaceContainerHigh = Color.Unspecified,
        surfaceContainerHighest = Color.Unspecified,
        surfaceContainerLow = Color.Unspecified,
        surfaceContainerLowest = Color.Unspecified,
    )
}

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
    outline = this.outline,
    outlineVariant = this.outlineVariant,
    scrim = this.scrim,
    surfaceBright = this.surfaceBright,
    surfaceDim = this.surfaceDim,
    surfaceContainer = this.surfaceContainer,
    surfaceContainerHigh = this.surfaceContainerHigh,
    surfaceContainerHighest = this.surfaceContainerHighest,
    surfaceContainerLow = this.surfaceContainerLow,
    surfaceContainerLowest = this.surfaceContainerLowest,
)
