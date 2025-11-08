package com.rodrigmatrix.weatheryou.domain.model

enum class WeatherCard(
    val fullSpan: Boolean,
) {
    Hours(
        fullSpan = true,
    ),
    FutureDays(
        fullSpan = true,
    ),
    Wind(
        fullSpan = false,
    ),
    FeelsLike(
        fullSpan = false,
    ),
    Visibility(
        fullSpan = false,
    ),
    UvIndex(
        fullSpan = false,
    ),
    Pressure(
        fullSpan = false,
    ),
    Humidity(
        fullSpan = false,
    ),
    SunriseSunset(
        fullSpan = true,
    ),
}