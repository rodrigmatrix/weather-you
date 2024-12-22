package com.rodrigmatrix.weatheryou.settings.presentation.settings

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.components.location.RequestBackgroundLocationDialog
import com.rodrigmatrix.weatheryou.settings.presentation.settings.component.SwitchWithDescription
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.toOption
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = getViewModel(),
    onFetchLocations: () -> Unit,
    backgroundLocationPermissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            android.Manifest.permission.ACCESS_FINE_LOCATION
        },
        onPermissionResult = {
            viewModel.onPermissionChanged()
        }
    ),
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                viewModel.onDialogStateChanged(SettingsDialogState.BackgroundLocation)
            } else {
                viewModel.onPermissionChanged()
            }
        }
    ),
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()

    SettingsScreen(
        viewState,
        onEditUnits = viewModel::onEditUnit,
        onEditTheme = viewModel::onEditTheme,
        onNewUnit = {
            viewModel.onNewUnit(it)
            onFetchLocations()
        },
        onNewColor = viewModel::onNewColorTheme,
        onNewTheme = viewModel::onNewTheme,
        onWeatherAnimationsChange = viewModel::onWeatherAnimationsChange,
        onDismissDialog = viewModel::hideDialogs,
        onDialogStateChanged = viewModel::onDialogStateChanged,
        requestBackgroundPermission = {
            backgroundLocationPermissionState.launchPermissionRequest()
        },
        requestLocationPermission = {
            locationPermissionState.launchMultiplePermissionRequest()
        },
        onPermissionChanged = {
            viewModel.onPermissionChanged()
        },
    )

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { viewEffect ->
            when (viewEffect) {
                SettingsViewEffect.OnPermissionChanged -> {
                    viewModel.updateLocationsState(
                        hasBackgroundLocationPermission = hasBackgroundLocationPermission(context),
                        hasLocationPermission = hasLocationPermission(context),
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.updateLocationsState(
            hasBackgroundLocationPermission = hasBackgroundLocationPermission(context),
            hasLocationPermission = hasLocationPermission(context),
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    onEditUnits: () -> Unit,
    onEditTheme: () -> Unit,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onNewTheme: (AppThemePreferenceOption) -> Unit,
    onNewColor: (AppColorPreferenceOption) -> Unit,
    onWeatherAnimationsChange: (Boolean) -> Unit,
    onDismissDialog: () -> Unit,
    onDialogStateChanged: (SettingsDialogState) -> Unit,
    requestBackgroundPermission: () -> Unit,
    requestLocationPermission: () -> Unit,
    onPermissionChanged: () -> Unit,
) {
    when (viewState.dialogState) {
        SettingsDialogState.HIDDEN -> Unit
        SettingsDialogState.THEME -> ThemeAndColorModeSelector(
            themeMode = viewState.appSettings.appThemePreference.toOption().option,
            colorMode = viewState.appSettings.appColorPreference.toOption().option,
            onThemeModeChange = onNewTheme,
            onColorChange = onNewColor,
            onClose = onDismissDialog,
        )
        SettingsDialogState.UNITS -> UnitsDialog(
            selected = viewState.appSettings.temperaturePreference.toOption(),
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissDialog
        )

        SettingsDialogState.BackgroundLocation -> RequestBackgroundLocationDialog(
            onRequestPermissionClicked = {
                requestBackgroundPermission()
                onPermissionChanged()
                onDialogStateChanged(SettingsDialogState.HIDDEN)
            },
            onDismissRequest = {
                onPermissionChanged()
                onDialogStateChanged(SettingsDialogState.HIDDEN)
            },
        )

    }
    Column(
        Modifier
            .fillMaxSize()
            .background(WeatherYouTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.units))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.units),
            selected = stringResource(viewState.appSettings.temperaturePreference.toOption().title),
            onClick = onEditUnits,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.appearance))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.app_theme),
            selected = stringResource(viewState.appSettings.appThemePreference.toOption().title),
            onClick = onEditTheme,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SwitchWithDescription(
            checked = viewState.appSettings.enableWeatherAnimations,
            description = stringResource(R.string.enable_weather_animations),
            onCheckedChange = { onWeatherAnimationsChange(!viewState.appSettings.enableWeatherAnimations) },
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(Modifier.height(10.dp))
        if (!viewState.hasLocationPermission) {
            SettingTitle(stringResource(R.string.location))
            Spacer(Modifier.height(10.dp))
            Text(
                text = "You don't have location permissions set up. You will not be able to see the weather for your current location, update local weather widget or get weather alerts. If you like those feature, please enable the permission",
                style = WeatherYouTheme.typography.titleMedium,
                color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = requestLocationPermission,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Enable location permission"
                )
            }
        } else if (!viewState.hasBackgroundLocationPermission) {
            SettingTitle(stringResource(R.string.location))
            Spacer(Modifier.height(10.dp))
            Text(
                text = "You don't have background location permissions set up. You will not be able to see the weather for your current location, update local weather widget or get weather alerts. If you like those feature, please enable the permission",
                style = WeatherYouTheme.typography.titleMedium,
                color = WeatherYouTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = requestBackgroundPermission,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Enable background location permission"
                )
            }
        }
//        Spacer(Modifier.height(10.dp))
//        SwitchWithDescription(
//            checked = viewState.appSettings.enableThemeColorWithWeatherAnimations,
//            description = stringResource(R.string.enable_theme_color_inside_weather_animations),
//            onCheckedChange = { onWeatherAnimationsChange(!viewState.appSettings.enableWeatherAnimations) },
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
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
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            color = WeatherYouTheme.colorScheme.secondaryContainer,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.units),
                    style = WeatherYouTheme.typography.headlineSmall
                )
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(TemperaturePreferenceOption.entries.toTypedArray()) {
                        UnitChoiceItem(
                            option = it,
                            selected = it == selected,
                            onNewUnit
                        )
                    }
                }
            }
        }
    }
}

private fun hasBackgroundLocationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ActivityCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    } else {
        ActivityCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(context,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeChoiceItem(
    option: AppThemePreferenceOption,
    selected: Boolean,
    onClick: (AppThemePreferenceOption) -> Unit
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UvIndexCardPreview() {
    WeatherYouTheme {
        SettingsScreen(
            viewState = SettingsViewState(),
            onEditUnits = { },
            onEditTheme = { },
            onNewUnit = { },
            onNewTheme = { },
            onNewColor = { },
            onDismissDialog = { },
            onWeatherAnimationsChange = { },
            onPermissionChanged = { },
            onDialogStateChanged = { },
            requestBackgroundPermission = { },
            requestLocationPermission = { },
        )
    }
}