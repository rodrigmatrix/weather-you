package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults.IconSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.R

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    onSearchButtonClicked: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions(
        onSearch = {
            onSearchButtonClicked()
        },
    ),
) {
    Surface(
        color = WeatherYouTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(48.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    return@onKeyEvent false
                }
                if (it.key == Key.DirectionDown) {
                    onSearchFocusChange(true)
                }
                return@onKeyEvent false
            }
    ) {
        Box(Modifier.fillMaxSize()) {
            if (query.isEmpty()) {
                SearchHint()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
            ) {
                val icon = if (query.isNotEmpty()) {
                    Icons.Outlined.Clear
                } else {
                    Icons.AutoMirrored.Outlined.ArrowBack
                }
                if (showBackButton) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            icon,
                            tint = WeatherYouTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                } else if (query.isNotEmpty()) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            imageVector =  Icons.Outlined.Clear,
                            tint = WeatherYouTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                } else {
                    Spacer(Modifier.size(24.dp))
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = WeatherYouTheme.typography.bodyMedium
                        .copy(color = WeatherYouTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = keyboardActions,
                    cursorBrush = SolidColor(WeatherYouTheme.colorScheme.onSurface),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                if (searching) {
                    CircularProgressIndicator(
                        color = WeatherYouTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(36.dp)
                    )
                } else if (query.isNotEmpty()) {
                    IconButton(onClick = onSearchButtonClicked) {
                        Icon(
                            imageVector =  Icons.Outlined.Search,
                            tint = WeatherYouTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.label_search)
                        )
                    }
                } else {
                    Spacer(Modifier.width(IconSize))
                }
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            tint = WeatherYouTheme.colorScheme.onSurfaceVariant,
            contentDescription = stringResource(R.string.label_search)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.search_location),
            color = WeatherYouTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        query = "",
        onQueryChange = { },
        onSearchFocusChange = { },
        onClearQuery = { },
        onSearchButtonClicked = { },
        searching = false
    )
}