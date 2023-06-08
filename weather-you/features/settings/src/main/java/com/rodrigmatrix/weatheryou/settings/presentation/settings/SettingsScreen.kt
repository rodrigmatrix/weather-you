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
import androidx.compose.material3.MaterialTheme
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
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.AppThemePreferenceOption
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    SettingsScreen(
        viewState,
        onEditUnits = viewModel::onEditUnit,
        onEditTheme = viewModel::onEditTheme,
        onNewUnit = {
            viewModel.onNewUnit(it)
        },
        onNewTheme = {
            viewModel.onNewTheme(it)
        },
        onDismissDialog = viewModel::hideDialogs
    )
}

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    onEditUnits: () -> Unit,
    onEditTheme: () -> Unit,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onNewTheme: (AppThemePreferenceOption) -> Unit,
    onDismissDialog: () -> Unit
) {
    if (viewState.unitsDialogVisible) {
        UnitsDialog(
            selected = viewState.selectedTemperature,
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissDialog
        )
    }
    if (viewState.themeDialogVisible) {
        ThemeDialog(
            selected = viewState.selectedAppTheme,
            onNewTheme = onNewTheme,
            onDismissRequest = onDismissDialog
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
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
            color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.units),
                    style = MaterialTheme.typography.headlineSmall
                )
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(TemperaturePreferenceOption.values()) {
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
            color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_theme),
                    style = MaterialTheme.typography.headlineSmall
                )
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(AppThemePreferenceOption.values()) {
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
            style = MaterialTheme.typography.titleMedium,
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
            style = MaterialTheme.typography.titleMedium,
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
    MaterialTheme {
        SettingsScreen(
            viewState = SettingsViewState(),
            onEditUnits = { },
            onEditTheme = { },
            onNewUnit = { },
            onNewTheme = { },
            onDismissDialog = { }
        )
    }
}