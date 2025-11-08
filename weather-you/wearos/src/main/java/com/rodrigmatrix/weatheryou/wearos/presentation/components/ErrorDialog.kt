package com.rodrigmatrix.weatheryou.wearos.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

@Composable
fun ErrorDialog(
    error: String,
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Confirmation(
        icon = {
            Icon(
                painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_error),
                tint = MaterialTheme.colors.error,
                contentDescription = "error",
                modifier = Modifier.size(32.dp)
            )
        },
        onTimeout = onTimeout,
        durationMillis = 3000L,
        modifier = modifier,
    ) {
        Text(
            text = error,
            textAlign = TextAlign.Center,
        )
    }
}