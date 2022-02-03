package com.rodrigmatrix.weatheryou.presentation.tv.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme

class HomeTvActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           HomeTv()
        }
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun HomeTvPreview() {
    WeatherYouTheme(darkTheme = true) {
        HomeTv()
    }
}