package com.rodrigmatrix.weatheryou.tv.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardGlow
import androidx.tv.material3.CardScale
import androidx.tv.material3.CardShape
import androidx.tv.material3.Glow
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.tv.presentation.theme.md_theme_dark_secondaryContainer

@Composable
fun TvCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onLongClick: (() -> Unit)? = null,
    shape: CardShape = CardDefaults.shape(),
    colors: CardColors = CardDefaults.colors(
        containerColor =  if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
            md_theme_dark_secondaryContainer.copy(alpha = 0.4f)
        } else {
            WeatherYouTheme.colorScheme.secondaryContainer
        },
    ),
    scale: CardScale = CardDefaults.scale(
        focusedScale = 1.08f,
    ),
    border: CardBorder = CardDefaults.border(),
    glow: CardGlow = CardDefaults.glow(
        focusedGlow = Glow(
            elevationColor = if (WeatherYouTheme.themeSettings.showWeatherAnimations) {
                md_theme_dark_secondaryContainer.copy(alpha = 0.4f)
            } else {
                WeatherYouTheme.colorScheme.secondaryContainer
            },
            elevation = 4.dp,
        )
    ),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        onLongClick = onLongClick,
        shape = shape,
        colors = colors,
        scale = scale,
        border = border,
        glow = glow,
        interactionSource = interactionSource,
        content = content,
    )
}