package com.rodrigmatrix.weatheryou.presentation.about

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.about.model.SocialNetwork
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme

@Composable
fun AboutScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.image_about),
            contentDescription = stringResource(R.string.image_of_developer),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
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
            text = stringResource(R.string.developed_by),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.about_description),
            style = MaterialTheme.typography.bodyLarge,
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
        color = MaterialTheme.colorScheme.secondaryContainer,
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