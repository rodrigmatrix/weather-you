package com.rodrigmatrix.weatheryou.settings.presentation.settings

import android.os.Build
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.components.theme.DarkColorScheme
import com.rodrigmatrix.weatheryou.components.theme.LightColorScheme
import com.rodrigmatrix.weatheryou.components.theme.carmineDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.carmineLightScheme
import com.rodrigmatrix.weatheryou.components.theme.cinnamonDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.cinnamonLightScheme
import com.rodrigmatrix.weatheryou.components.theme.darkFernDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.darkFernLightScheme
import com.rodrigmatrix.weatheryou.components.theme.freshEggplantDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.freshEggplantLightScheme
import com.rodrigmatrix.weatheryou.components.theme.gigasDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.gigasLightScheme
import com.rodrigmatrix.weatheryou.components.theme.mosqueDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.mosqueLightScheme
import com.rodrigmatrix.weatheryou.components.theme.peruTanDarkScheme
import com.rodrigmatrix.weatheryou.components.theme.peruTanLightScheme

@Composable
fun ThemeAndColorModeSelector(
    themeMode: ThemeMode,
    colorMode: ColorMode,
    onClose: () -> Unit,
    onColorChange: (AppColorPreferenceOption) -> Unit,
    onThemeModeChange: (AppThemePreferenceOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onClose,
    ) {
        if (true) {
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.END)
        }
        Column(modifier) {
            Surface(
                color = WeatherYouTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 6.dp,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .heightIn(max = 600.dp)
                    .width(300.dp)
                    .padding(12.dp)
                    .align(Alignment.End),
            ) {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    item {
                        Text(
                            text = stringResource(R.string.theme_and_color_mode),
                            color = WeatherYouTheme.colorScheme.onSurface,
                            style = WeatherYouTheme.typography.titleMedium,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 20.sp
                        )
                    }

                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.app_theme),
                                color = WeatherYouTheme.colorScheme.onSurface,
                                style = WeatherYouTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                fontSize = 12.sp
                            )

                            Column(
                                Modifier.selectableGroup()
                            ) {
                                AppThemePreferenceOption.entries.forEach {
                                    val isSelected = it.option == themeMode
                                    Surface(
                                        selected = isSelected,
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) {
                                            WeatherYouTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                        } else {
                                            WeatherYouTheme.colorScheme.surface
                                        },
                                        onClick = { onThemeModeChange(it) },
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .semantics(mergeDescendants = true) { },
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp),
                                        ) {
                                            Icon(
                                                painter = painterResource(it.icon),
                                                contentDescription = null
                                            )
                                            Text(
                                                text = stringResource(it.title),
                                                style = WeatherYouTheme.typography.bodyMedium,
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp)
                                                    .weight(1f),
                                            )
                                            RadioButton(
                                                selected = isSelected,
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = WeatherYouTheme.colorScheme.primary,
                                                    unselectedColor = WeatherYouTheme.colorScheme.secondary,
                                                ),
                                                onClick = { onThemeModeChange(it) },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.color_mode),
                                style = WeatherYouTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                fontSize = 12.sp
                            )

                            Column(
                                Modifier.selectableGroup()
                            ) {
                                val dropNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    0
                                } else {
                                    1
                                }
                                AppColorPreferenceOption.entries.drop(dropNumber).forEach {
                                    val isSelected = colorMode == it.option
                                    Surface(
                                        selected = isSelected,
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) {
                                            WeatherYouTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                        } else {
                                            WeatherYouTheme.colorScheme.surface
                                        },
                                        onClick = { onColorChange(it) },
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .semantics(mergeDescendants = true) { },
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .background(
                                                        it.colorScheme().primaryContainer,
                                                        CircleShape
                                                    )
                                            )
                                            Text(
                                                text = stringResource(it.title),
                                                style = WeatherYouTheme.typography.bodyMedium,
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp)
                                                    .weight(1f),
                                            )
                                            RadioButton(
                                                selected = isSelected,
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = it.colorScheme().primaryContainer,
                                                    unselectedColor = it.colorScheme().primaryContainer
                                                ),
                                                onClick = { onColorChange(it) },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppColorPreferenceOption.colorScheme() = when (this) {
    AppColorPreferenceOption.DYNAMIC -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        when {
            WeatherYouTheme.themeMode == ThemeMode.Dark -> dynamicDarkColorScheme(LocalContext.current)
            else -> dynamicLightColorScheme(LocalContext.current)
        }
    } else {
        when {
            WeatherYouTheme.themeMode == ThemeMode.Dark -> DarkColorScheme
            else -> LightColorScheme
        }
    }
    AppColorPreferenceOption.DEFAULT -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> DarkColorScheme
        else -> LightColorScheme
    }
    AppColorPreferenceOption.MOSQUE -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> mosqueLightScheme
        else -> mosqueDarkScheme
    }
    AppColorPreferenceOption.DARK_FERN -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> darkFernDarkScheme
        else -> darkFernLightScheme
    }
    AppColorPreferenceOption.FRESH_EGGPLANT -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> freshEggplantDarkScheme
        else -> freshEggplantLightScheme
    }
    AppColorPreferenceOption.CARMINE -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> carmineDarkScheme
        else -> carmineLightScheme
    }
    AppColorPreferenceOption.CINNAMON -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> cinnamonDarkScheme
        else -> cinnamonLightScheme
    }
    AppColorPreferenceOption.PERU_TAN -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> peruTanDarkScheme
        else -> peruTanLightScheme
    }
    AppColorPreferenceOption.GIGAS -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> gigasDarkScheme
        else -> gigasLightScheme
    }
}

@Preview
@Composable
private fun ThemeAndColorModeSelectorLightPreview() {
    WeatherYouTheme(
        themeMode = ThemeMode.Light,
        colorMode = ColorMode.FreshEggplant
    ) {
        ThemeAndColorModeSelector(
            themeMode = ThemeMode.Light,
            colorMode = ColorMode.FreshEggplant,
            onClose = {},
            onColorChange = {},
            onThemeModeChange = { },
        )
    }
}

@Preview
@Composable
private fun ThemeAndColorModeSelectorDarkPreview() {
    WeatherYouTheme(
        themeMode = ThemeMode.Dark,
        colorMode = ColorMode.FreshEggplant
    ) {
        ThemeAndColorModeSelector(
            themeMode = ThemeMode.Dark,
            colorMode = ColorMode.FreshEggplant,
            onClose = {},
            onColorChange = {},
            onThemeModeChange = { },
        )
    }
}
