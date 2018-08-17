@file:JvmName("Constants")

package com.foretree.support.emoji

import android.content.Context
import android.content.res.Resources

/**
 * Created by silen on 16/08/2018
 */

val regx = "\\[[^]]+]"

fun dip2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun getMetricsWidth(context: Context?): Int {
    return context?.resources?.displayMetrics?.widthPixels ?: 0
}

fun getMetricsHeight(context: Context?): Int {
    return context?.resources?.displayMetrics?.heightPixels ?: 0
}