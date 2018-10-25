package com.hamletleon.moviewratings.utils

import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

fun AppCompatImageView?.load(path: String?, size: String = "w185", request: (RequestCreator) -> RequestCreator = {r -> r}) {
    if (this == null || path.isNullOrBlank()) return
    request(Picasso.get().load("http://image.tmdb.org/t/p/$size$path")).into(this)
}

fun AppCompatImageView?.setColorTint(@ColorRes colorRes: Int) {
    if (this == null) return
    val color = ContextCompat.getColor(this.context, colorRes)
    this.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
}