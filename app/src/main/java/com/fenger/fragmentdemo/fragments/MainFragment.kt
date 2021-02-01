package com.fenger.fragmentdemo.fragments

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fenger.fragmentdemo.spanutils.NoCopySpanEditableFactory
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.spanutils.SelectionSpanWatcher
import com.fenger.fragmentdemo.permission.PermissionHelper

/**
 * com.fenger.fragmentdemo.MainFragment
 * Created by fenger
 * in 2020-01-02
 */
class MainFragment : Fragment() {
    private lateinit var textView: TextView
    private lateinit var stringBuilder: SpannableStringBuilder

    companion object {
        private const val CODE_CALL_PHONE = 0x0011
        private const val SIZE_IN_END = 8
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        textView = view.findViewById(R.id.text)
        stringBuilder = SpannableStringBuilder("今天是个好日子，花儿为啥这么红")
        stringBuilder.setSpan(ForegroundColorSpan(Color.RED), 2, SIZE_IN_END, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = stringBuilder
        textView.setEditableFactory(NoCopySpanEditableFactory(SelectionSpanWatcher(ForegroundColorSpan(Color.RED))))
        textView.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                onDelDown((v as EditText).text)
            }
            false
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setOnClickListener {
            PermissionHelper.with(this).requestCode(CODE_CALL_PHONE).
            requestPermission(arrayOf(Manifest.permission.CALL_PHONE)).request()
            Log.d("fenger", "onClick: 这里是外部的点击事件")
        }
        return view
    }

    private fun onDelDown(text: Spannable): Boolean {
        val selectionStart = Selection.getSelectionStart(text)
        val selectionEnd = Selection.getSelectionEnd(text)
        text.getSpans(selectionStart, selectionEnd, ForegroundColorSpan::class.java)
                .firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {
            return (selectionStart == selectionEnd).also {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                Selection.setSelection(text, spanStart, spanEnd)
            }
        }
        return false
    }
}
