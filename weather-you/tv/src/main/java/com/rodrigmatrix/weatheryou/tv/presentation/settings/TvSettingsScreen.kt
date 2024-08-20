package com.rodrigmatrix.weatheryou.tv.presentation.settings

import android.content.res.Configuration
import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
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
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsDialogState
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewState
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTvTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun TvSettingsScreen(
    modifier: Modifier = Modifier,
    viewmodel: SettingsViewModel = getViewModel(),
) {
    val viewState by viewmodel.viewState.collectAsState()

    TvSettingsScreen(
        viewState = viewState,
        onClose = viewmodel::hideDialogs,
        onColorChange = viewmodel::onNewColorTheme,
        onThemeModeChange = viewmodel::onNewTheme,
        onNewUnit = viewmodel::onNewUnit,
        onEditUnits = viewmodel::onEditUnit,
        onEditTheme = viewmodel::onEditTheme,
        onDismissDialog = viewmodel::hideDialogs,
        modifier = modifier,
    )
}

@Composable
fun TvSettingsScreen(
    viewState: SettingsViewState,
    onClose: () -> Unit,
    onColorChange: (AppColorPreferenceOption) -> Unit,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onThemeModeChange: (AppThemePreferenceOption) -> Unit,
    onEditUnits: () -> Unit,
    onEditTheme: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (viewState.dialogState) {
        SettingsDialogState.HIDDEN -> Unit
        SettingsDialogState.THEME -> TvThemeAndColorModeSelector(
            themeMode = viewState.selectedAppTheme.option,
            colorMode = viewState.selectedColor.option,
            onClose = onClose,
            onColorChange = onColorChange,
            onThemeModeChange = onThemeModeChange,
        )
        SettingsDialogState.UNITS -> UnitsDialog(
            selected = viewState.selectedTemperature,
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissDialog
        )
    }
    Column(
        modifier
            .fillMaxSize()
            .padding(28.dp)
            .background(WeatherYouTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.units))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.units),
            selected = stringResource(viewState.selectedTemperature.title),
            onClick = onEditUnits,
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_square_foot,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.appearance))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.theme_and_color_mode),
            selected = stringResource(R.string.app_theme) + ": " +
                    stringResource(viewState.selectedAppTheme.title) + " " +
                    stringResource(R.string.color_mode) + ": " +
                    stringResource(viewState.selectedColor.title),
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_palette,
            onClick = onEditTheme,
            modifier = Modifier
        )
    }
}

@Composable
fun SettingTitle(
    title: String
) {
    Text(
        text = title,
        style = WeatherYouTheme.typography.titleLarge,
        color = WeatherYouTheme.colorScheme.primary.copy(alpha = 0.7f),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    )
}

@Composable
fun UnitsDialog(
    selected: TemperaturePreferenceOption,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.END)
        Column(
            modifier = Modifier.onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
                    onDismissRequest()
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
                    modifier = Modifier.padding(12.dp),
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.units),
                            color = WeatherYouTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 20.sp
                        )
                    }

                    item {
                        Column {

                            Column(
                                Modifier.selectableGroup()
                            ) {
                                TemperaturePreferenceOption.entries.toTypedArray().forEach {
                                    val isSelected = it.option == selected.option
                                    ListItem(
                                        selected = isSelected,
                                        onClick = { onNewUnit(it) },
                                        modifier = Modifier.semantics(mergeDescendants = true) { },
                                        colors = ListItemDefaults.colors(

                                        ),
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
                }
            }
        }
    }
}

@Composable
fun UnitChoiceItem(
    option: TemperaturePreferenceOption,
    selected: Boolean,
    onClick: (TemperaturePreferenceOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = {
                    onClick(option)
                }
            )
    ) {
        Text(
            text = stringResource(option.title),
            style = WeatherYouTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
        RadioButton(
            selected = selected,
            onClick = {
                onClick(option)
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun SettingWithOption(
    title: String,
    @DrawableRes icon: Int,
    selected: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        selected = false,
        onClick = onClick,
        headlineContent = {
            Text(
                text = title,
                style = WeatherYouTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = selected,
                style = WeatherYouTheme.typography.titleMedium,
                modifier = Modifier.alpha(0.7f)
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(ListItemDefaults.IconSize)
            )
        },
        modifier = modifier,
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TvSettingsScreenPreview() {
    WeatherYouTvTheme {

    }
}