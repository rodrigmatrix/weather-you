package com.rodrigmatrix.weatheryou.domain.model

data class Hour(
    val temperature: Double,
    val icon: Int,
    val dateTime: String,
    val weatherCondition: String
)