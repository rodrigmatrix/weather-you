package com.rodrigmatrix.weatheryou.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rodrigmatrix.weatheryou.domain.R

@Composable
fun DeleteLocationDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissButtonClick,
        title = {
            Text(text = stringResource(R.string.delete_location_title))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButtonClick
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}