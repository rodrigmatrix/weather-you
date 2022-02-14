package com.rodrigmatrix.weatheryou.core.extensions

import android.content.Context
import android.widget.Toast

fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}