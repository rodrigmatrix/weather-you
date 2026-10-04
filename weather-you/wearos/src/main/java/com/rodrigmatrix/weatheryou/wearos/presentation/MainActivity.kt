package com.rodrigmatrix.weatheryou.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.sync.refreshEntitlementFromPhone
import com.rodrigmatrix.weatheryou.wearos.sync.EntitlementRefreshResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.wearos.presentation.navigation.WeatherYouWearNavHost
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private var resumedOnce = false

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val settingsRepository = get<SettingsRepository>()
        setContent {
            WeatherYouTheme {
                val infiniteTransition = rememberInfiniteTransition(label = "particleTick")
                val particleTick by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1_000_000f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 100_000,
                            easing = LinearEasing,
                        ),
                        repeatMode = RepeatMode.Restart,
                    ),
                    label = "particleTick",
                )
                WatchEntitlementGate(
                    settingsRepository = settingsRepository,
                    content = { WeatherYouWearNavHost(particleTick = particleTick.toLong()) },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (resumedOnce) {
            refreshEntitlementFromPhone(this)
        } else {
            resumedOnce = true
        }
    }
}

@Composable
private fun WatchEntitlementGate(
    settingsRepository: SettingsRepository,
    content: @Composable () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isDonor by settingsRepository.getIsPremiumUser().collectAsState(initial = false)
    var isRefreshing by remember { mutableStateOf(true) }
    var refreshResult by remember { mutableStateOf<EntitlementRefreshResult?>(null) }

    val refreshEntitlement = {
        isRefreshing = true
        refreshEntitlementFromPhone(context) { result ->
            refreshResult = result
            isRefreshing = false
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) { refreshEntitlement() }

    if (isDonor) {
        content()
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val message = when (refreshResult) {
                EntitlementRefreshResult.PHONE_NOT_SYNCED -> R.string.watch_phone_not_synced
                EntitlementRefreshResult.UNAVAILABLE -> R.string.watch_phone_unavailable
                EntitlementRefreshResult.ENTITLED, EntitlementRefreshResult.NOT_ENTITLED, null ->
                    R.string.watch_donation_required
            }
            Text(
                text = stringResource(if (isRefreshing) R.string.watch_checking_entitlement else message),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
            )
            Chip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = refreshEntitlement,
                enabled = !isRefreshing,
                label = { Text(stringResource(R.string.watch_check_entitlement)) },
            )
        }
    }
}
