package com.rodrigmatrix.weatheryou.tv.presentation.settings

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.RadioButtonDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.components.theme.ThemeMode
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.tv.presentation.theme.mosqueDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.mosqueLightScheme
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.tv.presentation.theme.TvDarkColorScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.TvLightColorScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.carmineDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.carmineLightScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.cinnamonDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.cinnamonLightScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.darkFernDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.darkFernLightScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.freshEggplantDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.freshEggplantLightScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.gigasDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.gigasLightScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.peruTanDarkScheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.peruTanLightScheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TvThemeAndColorModeSelector(
    themeMode: AppThemePreference,
    colorMode: AppColorPreference,
    onClose: () -> Unit,
    onColorChange: (AppColorPreferenceOption) -> Unit,
    onThemeModeChange: (AppThemePreferenceOption) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    Dialog(
        onDismissRequest = onClose,
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.END)
        Column(
            modifier = Modifier.onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
                    onClose()
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            },
        ) {
            Surface(
                colors = SurfaceDefaults.colors(
                    containerColor = WeatherYouTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .padding(12.dp)
                    .align(Alignment.End),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(12.dp)
                        .focusRequester(focusRequester)
                        .focusProperties {
                            exit = {
                                onClose()
                                FocusRequester.Default
                            }
                        },
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.theme_and_color_mode),
                            color = WeatherYouTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 20.sp
                        )
                    }

                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.app_theme),
                                color = WeatherYouTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                fontSize = 12.sp
                            )

                            Column(
                                Modifier.selectableGroup()
                            ) {
                                AppThemePreferenceOption.entries.drop(1).forEach {
                                    val isSelected = it.option == themeMode
                                    ListItem(
                                        selected = isSelected,
                                        onClick = { onThemeModeChange(it) },
                                        modifier = Modifier.semantics(mergeDescendants = true) { },
                                        colors = ListItemDefaults.colors(

                                        ),
                                        leadingContent = {
                                            Icon(
                                                painter = painterResource(it.icon),
                                                contentDescription = null
                                            )
                                        },
                                        trailingContent = {
                                            RadioButton(
                                                selected = isSelected,
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = WeatherYouTheme.colorScheme.primary,
                                                    unselectedColor = WeatherYouTheme.colorScheme.secondary,
                                                ),
                                                onClick = { },
                                            )
                                        },
                                        headlineContent = {
                                            Text(
                                                text = stringResource(it.title),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        }
                                    )
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
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                fontSize = 12.sp
                            )

                            Column(
                                Modifier.selectableGroup()
                            ) {
                                AppColorPreferenceOption.entries.drop(1).forEach {
                                    val isSelected = colorMode == it.option
                                    ListItem(
                                        selected = isSelected,
                                        onClick = { onColorChange(it) },
                                        modifier = Modifier.semantics(mergeDescendants = true) { },
                                        leadingContent = {
                                            Box(
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .background(it.colorScheme().primaryContainer, CircleShape)
                                            )
                                        },
                                        trailingContent = {
                                            RadioButton(
                                                selected = isSelected,
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = it.colorScheme().primaryContainer,
                                                    unselectedColor = it.colorScheme().primaryContainer
                                                ),
                                                onClick = { },
                                            )
                                        },
                                        headlineContent = {
                                            Text(
                                                text = stringResource(it.title),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        }
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

@Composable
private fun AppColorPreferenceOption.colorScheme() = when (this) {
    AppColorPreferenceOption.DYNAMIC -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> TvDarkColorScheme
        else -> TvLightColorScheme
    }
    AppColorPreferenceOption.DEFAULT -> when {
        WeatherYouTheme.themeMode == ThemeMode.Dark -> TvDarkColorScheme
        else -> TvLightColorScheme
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
