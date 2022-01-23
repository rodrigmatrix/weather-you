package com.rodrigmatrix.weatheryou.domain.model

data class Day(
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val hours: List<Hour>
)