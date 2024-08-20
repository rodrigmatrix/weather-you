package com.rodrigmatrix.weatheryou.settings.presentation.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.ScreenNavigationType
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppColorPreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    navigationType: ScreenNavigationType,
    viewModel: SettingsViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    SettingsScreen(
        viewState,
        onEditUnits = viewModel::onEditUnit,
        onEditTheme = viewModel::onEditTheme,
        onNewUnit = viewModel::onNewUnit,
        onNewColor = viewModel::onNewColorTheme,
        onNewTheme = viewModel::onNewTheme,
        onDismissDialog = viewModel::hideDialogs,
        navigationType = navigationType,
    )
}

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    onEditUnits: () -> Unit,
    onEditTheme: () -> Unit,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onNewTheme: (AppThemePreferenceOption) -> Unit,
    onNewColor: (AppColorPreferenceOption) -> Unit,
    onDismissDialog: () -> Unit,
    navigationType: ScreenNavigationType,
) {
    when (viewState.dialogState) {
        SettingsDialogState.HIDDEN -> Unit
        SettingsDialogState.THEME -> ThemeAndColorModeSelector(
            themeMode = viewState.selectedAppTheme.option,
            colorMode = viewState.selectedColor.option,
            onThemeModeChange = onNewTheme,
            onColorChange = onNewColor,
            onClose = onDismissDialog,
            navigationType = navigationType,
        )
        SettingsDialogState.UNITS -> UnitsDialog(
            selected = viewState.selectedTemperature,
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissDialog
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(WeatherYouTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.units))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.units),
            selected = stringResource(viewState.selectedTemperature.title),
            onClick = onEditUnits,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        SettingTitle(stringResource(R.string.appearance))
        Spacer(Modifier.height(10.dp))
        SettingWithOption(
            title = stringResource(R.string.app_theme),
            selected = stringResource(viewState.selectedAppTheme.title),
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

@Composable
fun ThemeDialog(
    selected: AppThemePreferenceOption,
    onNewTheme: (AppThemePreferenceOption) -> Unit,
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
                    text = stringResource(R.string.app_theme),
                    style = WeatherYouTheme.typography.headlineSmall
                )
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(AppThemePreferenceOption.entries.toTypedArray()) {
                        ThemeChoiceItem(
                            option = it,
                            selected = it == selected,
                            onNewTheme
                        )
                    }
                }
            }
        }
    }
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
            navigationType = ScreenNavigationType.BOTTOM_NAVIGATION
        )
    }
}