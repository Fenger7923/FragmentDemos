package com.fenger.fragmentdemo.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.customview.LetterSideBar
import com.fenger.fragmentdemo.customview.LetterSideBar.TouchLetterListener

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class FindFragment : Fragment(), TouchLetterListener {
    private lateinit var textView: TextView
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            textView.visibility = View.GONE
        }
    }

    companion object {
        private const val MSG = 0x0011
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_find, container, false)
        textView = view.findViewById(R.id.text)
        val letterSideBar: LetterSideBar = view.findViewById(R.id.letter_side_bar)
        letterSideBar.setMyOnTouchListener(this)
        return view
    }

    override fun touch(letter: CharSequence?, isTouch: Boolean) {
        textView.text = letter
        if (isTouch) {
            textView.visibility = View.VISIBLE
        } else {
            mHandler.sendEmptyMessageDelayed(MSG, 1000)
        }
    }
}