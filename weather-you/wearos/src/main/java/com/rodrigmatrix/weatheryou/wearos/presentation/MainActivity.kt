package com.rodrigmatrix.weatheryou.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.wearos.presentation.components.WeatherIcon
import com.rodrigmatrix.weatheryou.wearos.presentation.home.HomeScreen
import com.rodrigmatrix.weatheryou.wearos.theme.WeatherYouTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                HomeScreen()
            }
        }
    }
}