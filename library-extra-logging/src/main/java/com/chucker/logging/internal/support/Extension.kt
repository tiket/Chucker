package com.chucker.logging.internal.support

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import androidx.core.text.getSpans
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.text.SimpleDateFormat

internal fun Context.showDialog(
    title: String,
    message: String,
    positiveButtonText: String?,
    negativeButtonText: String?,
    onPositiveClick: (() -> Unit)?,
    onNegativeClick: (() -> Unit)?
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClick?.invoke()
        }
        .setNegativeButton(negativeButtonText) { _, _ ->
            onNegativeClick?.invoke()
        }
        .show()
}

internal fun String.formatLog(): String {
    return try {
        JSONObject(this).toString(2)
    } catch (ignored: Exception) {
        this
    }
}

internal fun Long.formatDate(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this)
}

internal fun String.makeHighlightFrom(query: String): CharSequence {
    val spannableString = SpannableString(this)
    val backgroundSpans = spannableString.getSpans(
        0, spannableString.length, BackgroundColorSpan::class.java
    )

    for (span in backgroundSpans) {
        spannableString.removeSpan(span)
    }

    if (query.isBlank()) return spannableString

    var indexOfKeyword = spannableString.toString().indexOf(query)

    while (indexOfKeyword != -1) {
        spannableString.setSpan(
            BackgroundColorSpan(Color.YELLOW),
            indexOfKeyword,
            indexOfKeyword + query.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        indexOfKeyword = spannableString.toString().indexOf(query, indexOfKeyword + query.length)
    }

    return spannableString

}