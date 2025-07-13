package com.rodrigmatrix.weatheryou.wearos.complications

import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon.createWithResource
import androidx.compose.ui.graphics.toArgb
import androidx.wear.watchface.complications.data.ColorRamp
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.MonochromaticImageComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.RangedValueComplicationData.Companion.TYPE_RATING
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.details.getUvIndexGradientList
import com.rodrigmatrix.weatheryou.components.extensions.getStaticIcon
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.core.extensions.toTemperatureString
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.wearos.presentation.MainActivity
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class PrecipitationChanceComplicationProviderService : SuspendingComplicationDataSourceService(), KoinComponent {

    private val getLocationsUseCase: GetLocationsUseCase by inject()

    private fun openScreen(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)

        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return generateComplication(
            type = type,
            currentLocation = PreviewWeatherLocation,
        )
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        val currentLocation = getLocationsUseCase.invoke()
            .firstOrNull()
            ?.minByOrNull { it.isCurrentLocation } ?: return null
        return generateComplication(
            type = request.complicationType,
            currentLocation = currentLocation,
        )
    }

    private fun generateComplication(
        type: ComplicationType,
        currentLocation: WeatherLocation,
    ): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = currentLocation.precipitationProbability.toFloat(),
                    min = 0f,
                    max = 100f,
                    contentDescription = PlainComplicationText.Builder(
                        text = currentLocation.precipitationProbability.roundToInt().toString() + "%"
                    ).build()
                )
                    .setColorRamp(
                        ColorRamp(
                            getUvIndexGradientList().map { it.toArgb() }.toIntArray(),
                            true
                        )
                    )
                    .setValueType(TYPE_RATING)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = createWithResource(
                                this,
                                if (currentLocation.precipitationType == PrecipitationType.Snow) {
                                    com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_snow
                                } else {
                                    com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_rainyday
                                },
                            )
                        ).build()
                    )
                    .setText(
                        PlainComplicationText.Builder(
                            text = currentLocation.precipitationProbability.roundToInt().toString() + "%"
                        ).build()
                    )
                    .setTitle(
                        PlainComplicationText.Builder(
                            text = getString(
                                if (currentLocation.precipitationType == PrecipitationType.Snow) {
                                    R.string.condition_snow
                                } else {
                                    R.string.condition_rain
                                }
                            ),
                        ).build()
                    )
                    .setTapAction(openScreen())
                    .build()
            }

            else -> null
        }
    }
}