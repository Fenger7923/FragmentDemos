package com.fenger.fragmentdemo

import android.text.Selection
import android.text.Spannable
import android.text.style.ForegroundColorSpan


fun onDelDown(text: Spannable): Boolean {
    val selectionStart = Selection.getSelectionStart(text)
    val selectionEnd = Selection.getSelectionEnd(text)
    text.getSpans(selectionStart, selectionEnd, ForegroundColorSpan::class.java).firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {
        return (selectionStart == selectionEnd).also {
            val spanStart = text.getSpanStart(this)
            val spanEnd = text.getSpanEnd(this)
            Selection.setSelection(text, spanStart, spanEnd)
        }
    }
    return false
}

