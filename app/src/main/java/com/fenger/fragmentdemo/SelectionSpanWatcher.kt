package com.fenger.fragmentdemo

import android.text.Selection
import android.text.SpanWatcher
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import java.lang.Math.abs
import kotlin.reflect.KClass

class SelectionSpanWatcher(private val kClass: ForegroundColorSpan) : SpanWatcher {

    private var selStart = 0
    private var selEnd = 0
    override fun onSpanChanged(text: Spannable, what: Any, ostart: Int, oend: Int, nstart: Int, nend: Int) {
        if (what === Selection.SELECTION_END && selEnd != nstart) {
            selEnd = nstart
            text.getSpans(nstart, nend, kClass::class.java).firstOrNull()?.run {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                val index = if (abs(selEnd - spanEnd) > abs(selEnd - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, Selection.getSelectionStart(text), index)
            }
        }

        if (what === Selection.SELECTION_START && selStart != nstart) {
            selStart = nstart
            text.getSpans(nstart, nend, kClass::class.java).firstOrNull()?.run {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                val index = if (abs(selStart - spanEnd) > abs(selStart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, index, Selection.getSelectionEnd(text))
            }
        }
    }

    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) = Unit

    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) = Unit
}