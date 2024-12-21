package com.rodrigmatrix.weatheryou.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.catch(): Flow<T> = catch {
    throw it
}