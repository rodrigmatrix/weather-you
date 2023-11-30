package com.rodrigmatrix.weatheryou.tv.presentation.search

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ClickableSurfaceScale
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.domain.model.City
import kotlin.math.max


private val MinImageSize = 134.dp
private val CategoryShape = RoundedCornerShape(10.dp)
private const val CategoryTextProportion = 0.55f

@OptIn(ExperimentalFoundationApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun LocationSuggestions(
    suggestions: List<City>,
    onLocationClick: (City) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.famous_cities),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .wrapContentHeight()
        )
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(2),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            itemsIndexed(suggestions) { index, location ->
                LocationRow(
                    location = location,
                    gradient = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer),
                    onLocationClick = onLocationClick,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LocationRow(
    location: City,
    gradient: List<Color>,
    onLocationClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = ClickableSurfaceDefaults.shape(CategoryShape),
        colors = ClickableSurfaceDefaults.colors(),
        scale = ClickableSurfaceDefaults.scale(
            focusedScale = 1.01f
        ),
        glow = ClickableSurfaceDefaults.glow(
            glow = Glow(elevationColor = gradient.first(), elevation = 4.dp)
        ),
        onClick = { onLocationClick(location) },
    ) {
        Layout(
            modifier = modifier
                .aspectRatio(1.45f)
                .background(Brush.horizontalGradient(gradient)),
            content = {
                Text(
                    text = stringResource(location.name),
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(start = 8.dp),
                )
                AsyncImage(
                    model = location.image,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = stringResource(R.string.image_of_city, location.name),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = 100.dp,
                                topEnd = 0.dp,
                                bottomStart = 100.dp,
                                bottomEnd = 0.dp
                            )
                        )
                )
            }
        ) { measurables, constraints ->
            val textWidth = (constraints.maxWidth * CategoryTextProportion).toInt()
            val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))
            val imageSize = max(MinImageSize.roundToPx(), constraints.maxHeight)
            val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))
            layout(
                width = constraints.maxWidth,
                height = constraints.minHeight
            ) {
                textPlaceable.placeRelative(
                    x = 0,
                    y = (constraints.maxHeight - textPlaceable.height) / 2
                )
                imagePlaceable.placeRelative(
                    x = textWidth,
                    y = (constraints.maxHeight - imagePlaceable.height) / 2
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun LocationSuggestionsPreview() {
    MaterialTheme {
        LocationSuggestions(
            PreviewFamousCities,
            { }
        )
    }
}