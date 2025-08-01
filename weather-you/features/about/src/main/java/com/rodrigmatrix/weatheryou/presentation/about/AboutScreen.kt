package com.rodrigmatrix.weatheryou.presentation.about

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_C
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.about.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.R as Strings
import com.rodrigmatrix.weatheryou.presentation.about.model.SocialNetwork
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.rodrigmatrix.weatheryou.about.BuildConfig
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    settingsRepository: SettingsRepository = koinInject(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    var showPremiumDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    if (showPremiumDialog) {
        AlertDialog(
            onDismissRequest = { }
        ) {
            Column {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                )
                Button(
                    onClick = {
                        if (password == BuildConfig.PREMIUM_PASSWORD) {
                            coroutineScope.launch {
                                settingsRepository
                                    .setIsPremiumUser(true)
                                    .collect {
                                        showPremiumDialog = false
                                    }
                            }
                        }
                    }
                ) { }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(WeatherYouTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        var clicks by remember { mutableIntStateOf(0) }
        Spacer(Modifier.statusBarsPadding())
        Image(
            painter = painterResource(R.drawable.ic_about),
            contentDescription = stringResource(Strings.string.image_of_developer),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clickable(
                    onClick = {
                        clicks++
                        if (clicks >= 20) {
                            showPremiumDialog = true
                        }
                    }
                )
                .padding(top = 16.dp)
                .size(130.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        SocialCard(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(Strings.string.developed_by),
            style = WeatherYouTheme.typography.headlineSmall,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(Strings.string.about_description),
            style = WeatherYouTheme.typography.bodyLarge,
            color = WeatherYouTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
private fun SocialCard(modifier: Modifier = Modifier) {
    Surface(
        color = WeatherYouTheme.colorScheme.secondaryContainer,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(SocialNetwork.values()) { socialNetwork ->
                SocialNetwork(socialNetwork)
            }
        }
    }
}

@Composable
private fun SocialNetwork(socialNetwork: SocialNetwork) {
    val context = LocalContext.current
    Icon(
        painter = painterResource(socialNetwork.icon),
        tint = WeatherYouTheme.colorScheme.onSecondaryContainer,
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .padding(16.dp)
            .clickable {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(socialNetwork.link)
                ).run { context.startActivity(this) }
            }
    )
}

@Preview(name = "Light Theme")
@Preview(name = "Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Tablet", uiMode = Configuration.UI_MODE_NIGHT_YES, device = PIXEL_C)
@Composable
fun AboutScreenPreview() {
    WeatherYouTheme {
        AboutScreen()
    }
}