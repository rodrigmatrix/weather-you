package com.rodrigmatrix.weatheryou.domain.model

data class Day(
    val dateTime: String,
    val weatherCondition: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val icon: Int,
    val hours: List<Hour>
)