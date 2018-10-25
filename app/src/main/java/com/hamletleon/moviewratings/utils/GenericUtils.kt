package com.hamletleon.moviewratings.utils

import android.app.Activity
import android.util.DisplayMetrics
import java.text.NumberFormat
import kotlin.math.truncate

fun Activity?.getAppBarHeight(): Int {
    val styledAttributes = this?.theme?.obtainStyledAttributes(
            intArrayOf(android.R.attr.actionBarSize))
    val appBarHeight = styledAttributes?.getDimension(0, 0f)?.toInt()
    styledAttributes?.recycle()

    return appBarHeight ?: 0
}

data class ScreenSize(val height: Int, val width: Int)
data class ItemsOnScreen(val itemsOnHeight: Int, val itemsOnWidth: Int, val totalItemsOnScreen: Int)
fun Activity?.calculateScreenSizeAndItemsOnIt(itemSizeDpHeight: Int, itemSizeDpWidth: Int): Pair<ScreenSize, ItemsOnScreen> {
    val metrics = DisplayMetrics()
    this?.windowManager?.defaultDisplay?.getMetrics(metrics)
    val itemSizePxHeight = truncate(itemSizeDpHeight * metrics.density)
    val itemSizePxWidth = truncate(itemSizeDpWidth * metrics.density)

    val appBarHeight = this.getAppBarHeight()
    val height = truncate(((metrics.heightPixels - itemSizePxHeight - appBarHeight) / 2)).toInt()
    val width = truncate(((metrics.widthPixels - itemSizePxWidth) / 2)).toInt()

    val itemsH = truncate((metrics.heightPixels - appBarHeight) / itemSizePxHeight).toInt()
    val itemsW = truncate(metrics.widthPixels / itemSizePxWidth).toInt()
    val totalItems = (itemsH * itemsW)

    val screenSize = ScreenSize(height, width)
    val itemsOnScreen = ItemsOnScreen(itemsH, itemsW, totalItems)

    return Pair(screenSize, itemsOnScreen)
}

val mFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
fun Int?.getCurrencyString(): String {
    val double = this?.toDouble() ?: 0.0
    return mFormatter.format(double)
}