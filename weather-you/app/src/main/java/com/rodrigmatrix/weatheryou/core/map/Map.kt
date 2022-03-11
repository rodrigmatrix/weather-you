package com.rodrigmatrix.weatheryou.core.map

abstract class Map<S, R> {

    abstract fun map(source: S): R
}