package com.rodrigmatrix.weatheryou.core.map

abstract class Mapper<S, R> {

    abstract fun map(source: S): R
}