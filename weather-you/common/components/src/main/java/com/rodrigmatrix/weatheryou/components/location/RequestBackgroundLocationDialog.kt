package com.rodrigmatrix.weatheryou.components.location

import android.view.Gravity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

@Composable
fun RequestBackgroundLocationDialog(
    onRequestPermissionClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)
        Card(
            modifier
                .width(400.dp)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Filled.Place,
                    tint = WeatherYouTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
                Text(
                    text = "Background Location Access",
                    textAlign = TextAlign.Center,
                    style = WeatherYouTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "We also need your permission for background location access. This will help us update your widgets and send weather alerts. This permission is not required but will impact those features if not enabled.",
                    textAlign = TextAlign.Center,
                    style = WeatherYouTheme.typography.bodyMedium,
                )

                TextButton(
                    onClick = onRequestPermissionClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Grant Permission",
                    )
                }
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Don't Allow",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RequestBackgroundLocationDialogPreview() {
    WeatherYouTheme {
        RequestBackgroundLocationDialog(
            onRequestPermissionClicked = {},
            onDismissRequest = {},
        )
    }
}