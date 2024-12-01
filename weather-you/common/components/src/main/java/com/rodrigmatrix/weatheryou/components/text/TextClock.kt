package com.rodrigmatrix.weatheryou.components.text

import android.graphics.Typeface
import android.text.format.DateFormat
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme

@Composable
fun TextClock(
    timeZone: String,
    modifier: Modifier = Modifier,
    format: String = "kk:mm",
    color: Color = Color.Unspecified,
    style: TextStyle = WeatherYouTheme.typography.labelLarge,
) {
    val context = LocalContext.current
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }
    val pattern = if (DateFormat.is24HourFormat(context)) {
        "HH:mm"
    } else {
        "hh:mm aa"
    }

    val resolver = LocalFontFamilyResolver.current
    val face: Typeface = remember(resolver, style) {
        resolver.resolve(
            fontFamily = style.fontFamily,
            fontWeight = style.fontWeight ?: FontWeight.Normal,
            fontStyle = style.fontStyle ?: FontStyle.Normal,
            fontSynthesis = style.fontSynthesis ?: FontSynthesis.All
        )
    }.value as Typeface

    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            android.widget.TextClock(viewContext).apply {
                format24Hour?.let {
                    this.format24Hour = format
                }

                format12Hour?.let {
                    this.format12Hour = format
                }

                this.timeZone = timeZone
                textSize.let { this.textSize = style.fontSize.value }

                setTextColor(textColor.toArgb())
                typeface = face
            }
        }
    )
}