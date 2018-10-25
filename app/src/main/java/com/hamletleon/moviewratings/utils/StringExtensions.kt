package com.hamletleon.moviewratings.utils

import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.hamletleon.moviewratings.R
import java.text.SimpleDateFormat
import java.util.*

fun String?.setTextView(textView: AppCompatTextView, @StringRes res: Int = R.string.not_specified) {
    if (this != null) textView.text = this
    else textView.setText(res)
}

fun String?.getFormattedDate(format: String = "dd MMMM yyyy"): String {
    val date = this.toDate()
    return if (date != null)
        SimpleDateFormat(format, Locale("es_DO")).format(date)
    else "No especificado"
}

fun String?.setDateToTextView(textView: AppCompatTextView, @StringRes res: Int = R.string.not_specified, format: String = "dd MMMM yyyy") {
    val date = this.getFormattedDate(format)
    date.setTextView(textView, res)
}