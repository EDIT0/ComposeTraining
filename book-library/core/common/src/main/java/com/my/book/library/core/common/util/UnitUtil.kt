package com.my.book.library.core.common.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

object UnitUtil {

    fun dpToPx(context: Context, dp: Float): Double {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toDouble()
    }

    fun pxToDp(context: Context, px: Float): Double {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toDouble()
    }

}