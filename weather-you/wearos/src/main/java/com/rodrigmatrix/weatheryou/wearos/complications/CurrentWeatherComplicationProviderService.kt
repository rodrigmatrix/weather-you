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
import com.rodrigmatrix.weatheryou.components.extensions.getStaticIcon
import com.rodrigmatrix.weatheryou.components.extensions.getString
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.core.extensions.toTemperatureString
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationsUseCase
import com.rodrigmatrix.weatheryou.wearos.presentation.MainActivity
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherComplicationProviderService : SuspendingComplicationDataSourceService(), KoinComponent {

    private val getLocationsUseCase: GetLocationsUseCase by inject()
    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()

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
            temperaturePreference = TemperaturePreference.METRIC,
        )
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        val currentLocation = getLocationsUseCase.invoke()
            .firstOrNull()
            ?.minByOrNull { it.isCurrentLocation } ?: return null
        val appSettings = getAppSettingsUseCase.invoke().firstOrNull() ?: return null
        return generateComplication(
            type = request.complicationType,
            currentLocation = currentLocation,
            temperaturePreference = TemperaturePreference.METRIC,
        )
    }

    private fun generateComplication(
        type: ComplicationType,
        currentLocation: WeatherLocation,
        temperaturePreference: TemperaturePreference,
    ): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = currentLocation.currentWeather.toFloat(),
                    min = currentLocation.lowestTemperature.toFloat(),
                    max = currentLocation.maxTemperature.toFloat(),
                    contentDescription = PlainComplicationText.Builder(
                        text = getString(
                            currentLocation.currentCondition.getString(
                                isDaylight = currentLocation.isDaylight,
                            )
                        )
                    ).build()
                )
                    .setColorRamp(
                        ColorRamp(
                            getTemperatureGradient(
                                minDayTemperature = currentLocation.lowestTemperature,
                                maxDayTemperature = currentLocation.maxTemperature,
                                hours = currentLocation.hours,
                            ).map { it.toArgb() }.toIntArray(),
                            true
                        )
                    )
                    .setValueType(TYPE_RATING)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = createWithResource(
                                this,
                                currentLocation.currentCondition.getStaticIcon(
                                    isDaylight = currentLocation.isDaylight
                                ),
                            )
                        ).build()
                    )
                    .setText(
                        PlainComplicationText.Builder(
                            text = currentLocation.currentWeather.toTemperatureString(
                                temperaturePreference = temperaturePreference
                            ),
                        ).build()
                    )
                    .setTitle(
                        PlainComplicationText.Builder(
                            text = getString(
                                currentLocation.currentCondition.getString(
                                    isDaylight = currentLocation.isDaylight,
                                )
                            )
                        ).build()
                    )
                    .setTapAction(openScreen())
                    .build()
            }
            ComplicationType.MONOCHROMATIC_IMAGE -> {
                MonochromaticImageComplicationData.Builder(
                    monochromaticImage = MonochromaticImage.Builder(
                        createWithResource(
                            this,
                            currentLocation.currentCondition.getStaticIcon(
                                isDaylight = currentLocation.isDaylight
                            )
                        )
                    ).build(),
                    contentDescription = PlainComplicationText.Builder(text = "")
                        .build()
                )
                    .setTapAction(openScreen())
                    .build()
            }

            ComplicationType.SMALL_IMAGE -> SmallImageComplicationData.Builder(
                smallImage = SmallImage.Builder(
                    image = createWithResource(
                        this,
                        currentLocation.currentCondition.getStaticIcon(currentLocation.isDaylight)
                    ),
                    type = SmallImageType.ICON
                ).build(),
                contentDescription = PlainComplicationText.Builder(text = "")
                    .build()
            )
                .setTapAction(openScreen())
                .build()

            else -> null
        }
    }
}