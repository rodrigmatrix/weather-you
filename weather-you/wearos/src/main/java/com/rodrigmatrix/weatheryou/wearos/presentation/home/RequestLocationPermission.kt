package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.rodrigmatrix.weatheryou.wearos.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permissionState: PermissionState,
    onLocationPermissionChanged: () -> Unit
) {
    when {
        permissionState.hasPermission -> {
            onLocationPermissionChanged()
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.location_request_title),
                        style = WeatherYouTheme.typography.title3,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.padding(bottom = 4.dp))
                    Button(
                        modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                        onClick = permissionState::launchPermissionRequest
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = stringResource(R.string.grant_location_permission)
                        )
                    }
                }
            }
        }
    }
}