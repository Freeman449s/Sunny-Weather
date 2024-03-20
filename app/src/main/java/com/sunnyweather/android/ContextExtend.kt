package com.sunnyweather.android

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt

/**
 * 封装使用resolveAttribute()解析attr形式颜色的过程
 */
@ColorInt
fun Context.getColorCompat(colorOrAttr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(colorOrAttr, typedValue, true)
    return typedValue.data
}