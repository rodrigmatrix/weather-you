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
import com.rodrigmatrix.weatheryou.settings.R
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
        onDismissRequest = {
            viewModel.onDismissDialog()
        }
    )
}

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    onEditUnits: () -> Unit,
    onNewUnit: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (viewState.unitsDialogVisible) {
        UnitsDialog(
            options = viewState.unitsList,
            selected = viewState.selectedUnit,
            onNewUnit = onNewUnit,
            onDismissRequest = onDismissRequest,

        )
    }
    Column(Modifier.fillMaxSize()) {
        SettingWithOption(
            title = stringResource(R.string.units),
            selected = viewState.selectedUnit,
            onClick = onEditUnits
        )
    }
}

@Composable
fun UnitsDialog(
    options: List<String>,
    selected: String,
    onNewUnit: (String) -> Unit,
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
                    items(options) {
                        ChoiceItem(it, it == selected, onNewUnit)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceItem(
    choice: String,
    selected: Boolean,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = {
                    onClick(choice)
                }
            )
    ) {
        Text(
            text = choice,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
        RadioButton(
            selected = selected,
            onClick = {
                onClick(choice)
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
            onDismissRequest = { }
        )
    }
}