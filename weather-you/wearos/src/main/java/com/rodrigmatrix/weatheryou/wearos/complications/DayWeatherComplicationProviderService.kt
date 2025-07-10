package com.rodrigmatrix.weatheryou.wearos.complications

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon.createWithResource
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.wear.watchface.complications.data.ColorRamp
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationText
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.MonochromaticImageComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.wearos.R

class DayWeatherComplicationProviderService : SuspendingComplicationDataSourceService() {

    private fun openScreen(): PendingIntent? {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/dev?id=5591589606735981545")
            //  "https://play.google.com/store/search?q=dev:amoledwatchfaces™")
            //"https://play.google.com/store/search?q=amoledwatchfaces™&c=apps")
            setPackage("com.android.vending")
        }

        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = 60f,
                    min = 20f,
                    max = 365f,
                    contentDescription = PlainComplicationText.Builder(text = getString(R.string.sunset)).build()
                )
                    .setColorRamp(ColorRamp(
                        intArrayOf(
                            Color.GREEN,
                            Color.YELLOW,
                            Color.argb(255, 255, 255, 0),
                            Color.RED,
                            Color.argb(255, 255, 0, 255),
                            Color.argb(255, 92, 64, 51)
                        ),
                        interpolated = true
                    ))
                    .setValueType(RangedValueComplicationData.TYPE_RATING)
                    .setMonochromaticImage(MonochromaticImage.Builder(image = createWithResource(this, R.drawable.ic_sunny)).build())
                    .setText(PlainComplicationText.Builder(text = "165").build())
                    .setTitle(PlainComplicationText.Builder(text = getString(R.string.sunrise)).build())
                    .setTapAction(openScreen())
                    .build()
            }
            ComplicationType.MONOCHROMATIC_IMAGE -> {
                MonochromaticImageComplicationData.Builder(
                    monochromaticImage = MonochromaticImage.Builder(createWithResource(this, R.drawable.ic_sunny)).build(),
                    contentDescription = ComplicationText.EMPTY)
                    .build()
            }
            ComplicationType.SMALL_IMAGE -> {
                SmallImageComplicationData.Builder(
                    smallImage = SmallImage.Builder(
                        image = createWithResource(this, R.drawable.ic_sunny),
                        type = SmallImageType.ICON).build(),
                    contentDescription = ComplicationText.EMPTY)
                    .build()
            }

            else -> {null}
        }
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return when (request.complicationType) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = 150f,
                    min = 20f,
                    max = 365f,
                    contentDescription = PlainComplicationText.Builder(text = getString(R.string.sunset)).build()
                )
                    .setColorRamp(ColorRamp(getTemperatureGradient(minDayTemperature = 16.0, maxDayTemperature = 24.0, emptyList()).map { it.toArgb() }.toIntArray(), true))
                    .setValueType(RangedValueComplicationData.TYPE_RATING)
                    .setMonochromaticImage(MonochromaticImage.Builder(image = createWithResource(this, R.drawable.ic_sunny)).build())
                    .setText(PlainComplicationText.Builder(text = "165").build())
                    .setTitle(PlainComplicationText.Builder(text = getString(R.string.sunrise)).build())
                    .setTapAction(openScreen())
                    .build()
            }
            ComplicationType.MONOCHROMATIC_IMAGE -> {
                MonochromaticImageComplicationData.Builder(
                    monochromaticImage = MonochromaticImage.Builder(
                        createWithResource(
                            this,
                            R.drawable.ic_sunny
                        )
                    ).build(),
                    contentDescription = PlainComplicationText.Builder(text = "amoledwatchfaces.com")
                        .build()
                )
                    .setTapAction(openScreen())
                    .build()
            }

            ComplicationType.SMALL_IMAGE -> SmallImageComplicationData.Builder(
                smallImage = SmallImage.Builder(
                    image = createWithResource(this, R.drawable.ic_sunny),
                    type = SmallImageType.ICON
                ).build(),
                contentDescription = PlainComplicationText.Builder(text = "amoledwatchfaces.com")
                    .build()
            )
                .setTapAction(openScreen())
                .build()

            else -> {
                null
            }
        }
    }
}