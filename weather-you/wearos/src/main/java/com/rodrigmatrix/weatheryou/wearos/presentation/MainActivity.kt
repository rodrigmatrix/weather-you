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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.wearos.presentation.navigation.WeatherYouWearNavHost
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
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
                WeatherYouWearNavHost(
                    particleTick = particleTick.toLong(),
                )
            }
        }
    }
}