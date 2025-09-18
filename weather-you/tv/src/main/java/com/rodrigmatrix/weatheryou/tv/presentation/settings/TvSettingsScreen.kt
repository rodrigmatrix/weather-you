package com.rodrigmatrix.weatheryou.tv.presentation.settings

import android.content.res.Configuration
import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.style.TextAlign
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
import androidx.tv.material3.Switch
import androidx.tv.material3.SwitchDefaults
import androidx.tv.material3.Text
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsDialogState
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewState
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.tv.presentation.theme.WeatherYouTvTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvSettingsScreen(
    modifier: Modifier = Modifier,
    viewmodel: SettingsViewModel = koinViewModel(),
) {
    val viewState by viewmodel.viewState.collectAsState()

    TvSettingsScreen(
        viewState = viewState,
        onClose = viewmodel::hideDialogs,
        onColorChange = viewmodel::onNewColorTheme,
        onThemeModeChange = viewmodel::onNewTheme,
        onSettingsChange = viewmodel::onSettingsUpdate,
        onDialogStateChange = viewmodel::onDialogStateChanged,
        onEditTheme = viewmodel::onEditTheme,
        onDismissDialog = viewmodel::hideDialogs,
        onWeatherAnimationsChange = viewmodel::onWeatherAnimationsChange,
        modifier = modifier,
    )
}

@Composable
fun TvSettingsScreen(
    viewState: SettingsViewState,
    onClose: () -> Unit,
    onDialogStateChange: (SettingsDialogState) -> Unit,
    onColorChange: (AppColorPreferenceOption) -> Unit,
    onSettingsChange: (AppSettings) -> Unit,
    onThemeModeChange: (AppThemePreferenceOption) -> Unit,
    onWeatherAnimationsChange: (Boolean) -> Unit,
    onEditTheme: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (viewState.dialogState) {
        SettingsDialogState.HIDDEN -> Unit
        SettingsDialogState.THEME -> TvThemeAndColorModeSelector(
            themeMode = viewState.appSettings.appThemePreference,
            colorMode = viewState.appSettings.appColorPreference,
            onClose = onClose,
            onColorChange = onColorChange,
            onThemeModeChange = onThemeModeChange,
        )
        SettingsDialogState.TemperatureUnit -> UnitsDialog(
            title = R.string.temperature_unit,
            entries = TemperaturePreference.entries,
            selected = viewState.appSettings.temperaturePreference,
            onNewUnit = {
                onSettingsChange(viewState.appSettings.copy(temperaturePreference = it))
            },
            itemTitle = { it.title },
            onDismissRequest = onDismissDialog,
        )
        SettingsDialogState.WindSpeedUnit -> UnitsDialog(
            title = R.string.wind_speed_unit,
            entries = WindUnitPreference.entries,
            selected = viewState.appSettings.windUnitPreference,
            onNewUnit = {
                onSettingsChange(viewState.appSettings.copy(windUnitPreference = it))
            },
            itemTitle = { it.title },
            onDismissRequest = onDismissDialog,
        )
        SettingsDialogState.PrecipitationUnit -> UnitsDialog(
            title = R.string.precipitation_unit,
            entries = PrecipitationUnitPreference.entries,
            selected = viewState.appSettings.precipitationUnitPreference,
            onNewUnit = {
                onSettingsChange(viewState.appSettings.copy(precipitationUnitPreference = it))
            },
            itemTitle = { it.title },
            onDismissRequest = onDismissDialog,
        )
        SettingsDialogState.DistanceUnit -> UnitsDialog(
            title = R.string.distance_unit,
            entries = DistanceUnitPreference.entries,
            selected = viewState.appSettings.distanceUnitPreference,
            onNewUnit = {
                onSettingsChange(viewState.appSettings.copy(distanceUnitPreference = it))
            },
            itemTitle = { it.title },
            onDismissRequest = onDismissDialog,
        )

        SettingsDialogState.BackgroundLocation -> Unit
    }
    Column(
        modifier
            .fillMaxSize()
            .padding(28.dp)
            .background(WeatherYouTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.units))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.temperature_unit),
            selected = stringResource(viewState.appSettings.temperaturePreference.title),
            onClick = {
                onDialogStateChange(SettingsDialogState.TemperatureUnit)
            },
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_thermostat,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.wind_speed_unit),
            selected = stringResource(viewState.appSettings.windUnitPreference.title),
            onClick = {
                onDialogStateChange(SettingsDialogState.WindSpeedUnit)
            },
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_air,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.precipitation_unit),
            selected = stringResource(viewState.appSettings.precipitationUnitPreference.title),
            onClick = {
                onDialogStateChange(SettingsDialogState.PrecipitationUnit)
            },
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_rainy,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.distance_unit),
            selected = stringResource(viewState.appSettings.distanceUnitPreference.title),
            onClick = {
                onDialogStateChange(SettingsDialogState.DistanceUnit)
            },
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_distance,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.appearance))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.theme_and_color_mode),
            selected = stringResource(R.string.app_theme) + ": " +
                    stringResource(viewState.appSettings.appThemePreference.title),
            icon = com.rodrigmatrix.weatheryou.settings.R.drawable.ic_palette,
            onClick = onEditTheme,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        TvSwitchWithDescription(
            checked = viewState.appSettings.enableWeatherAnimations,
            description = stringResource(R.string.enable_weather_animations),
            onCheckedChange = { onWeatherAnimationsChange(!viewState.appSettings.enableWeatherAnimations) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun TvSwitchWithDescription(
    checked: Boolean,
    description: String,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier
) {
    ListItem(
        selected = false,
        onClick = {
            onCheckedChange(checked.not())
        },
        headlineContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        trailingContent = {
            val icon: (@Composable () -> Unit)? = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                thumbContent = icon,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        },
        modifier = modifier,
    )
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
fun <T>UnitsDialog(
    title: Int,
    selected: T,
    itemTitle: (T) -> Int,
    onNewUnit: (T) -> Unit,
    entries: List<T>,
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
                            text = stringResource(title),
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
                                entries.forEach {
                                    val isSelected = it == selected
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
                                                text = stringResource(itemTitle(it)),
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
//
//@Composable
//fun <T>UnitChoiceItem(
//    option: T,
//    selected: Boolean,
//    onClick: (T) -> Unit,
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .selectable(
//                selected = selected,
//                onClick = {
//                    onClick(option)
//                }
//            )
//    ) {
//        Text(
//            text = stringResource(option.title),
//            style = WeatherYouTheme.typography.titleMedium,
//            modifier = Modifier
//                .padding(start = 16.dp)
//                .align(Alignment.CenterVertically)
//        )
//        RadioButton(
//            selected = selected,
//            onClick = {
//                onClick(option)
//            },
//            modifier = Modifier.align(Alignment.CenterVertically)
//        )
//    }
//}

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