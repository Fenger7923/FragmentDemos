package com.fenger.fragmentdemo.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020-01-06
 */
class MyViewPager constructor(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mListener?.setScroll(0)
            MotionEvent.ACTION_UP -> mListener?.setScroll(2500)
        }
        return super.dispatchTouchEvent(ev)
    }

    private var mListener: ScrollingListener? = null
    fun setScrollingListener(listener: ScrollingListener?) {
        mListener = listener
    }

    interface ScrollingListener {
        fun setScroll(scrollTime: Int)
    }
}
