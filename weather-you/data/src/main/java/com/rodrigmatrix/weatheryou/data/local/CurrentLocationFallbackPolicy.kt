package com.rodrigmatrix.weatheryou.data.local

internal object CurrentLocationFallbackPolicy {
    fun resolveName(addressName: String?, fallbackName: String): String =
        addressName?.takeIf(String::isNotBlank) ?: fallbackName
}
