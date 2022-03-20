package com.rodrigmatrix.weatheryou.wearos.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.wear.compose.foundation.CurvedRow
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.CurvedText
import androidx.wear.compose.material.LocalTextStyle
import androidx.wear.compose.material.Text

@Composable
fun CurvedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    val isRoundDevice = LocalConfiguration.current.isScreenRound
    if (isRoundDevice) {
        CurvedRow(modifier) {
            CurvedText(
                text = text,
                style = CurvedTextStyle(style)
            )
        }
    } else {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = style,
            )
        }
    }
}