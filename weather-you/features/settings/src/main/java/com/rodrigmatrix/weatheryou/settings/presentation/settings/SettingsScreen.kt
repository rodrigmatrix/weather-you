package com.rodrigmatrix.weatheryou.settings.presentation.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.settings.R
import com.rodrigmatrix.weatheryou.settings.presentation.settings.model.TemperaturePreferenceOption
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = getViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    SettingsScreen(
        viewState,
        onEditUnits = {
            viewModel.onEditUnits()
        },
        onNewUnit = {
            viewModel.onNewUnit(it)
        },
        onDismissDialog = {
            viewModel.hideDialogs()
        }
    )
}

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    onEditUnits: () -> Unit,
    onNewUnit: (TemperaturePreferenceOption) -> Unit,
    onDismissDialog: () -> Unit
) {
    if (viewState.unitsDialogVisible) {
        UnitsDialog(
            selected = viewState.selectedTemperature,
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissDialog
        )
    }
    Column(Modifier.fillMaxSize()) {
        SettingWithOption(
            title = stringResource(R.string.units),
            selected = stringResource(viewState.selectedTemperature.title),
            onClick = onEditUnits
        )
    }
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
                        ChoiceItem(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceItem(
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UvIndexCardPreview() {
    MaterialTheme {
        SettingsScreen(
            viewState = SettingsViewState(),
            onEditUnits = { },
            onNewUnit = { },
            onDismissDialog = { }
        )
    }
}