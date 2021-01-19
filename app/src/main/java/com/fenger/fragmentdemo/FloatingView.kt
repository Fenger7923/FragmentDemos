package com.fenger.fragmentdemo

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import com.fenger.fragmentdemo.FloatingManager.Companion.getInstance

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020/7/27
 */
/**
 * 悬浮窗view
 */
class FloatingView(private val mContext: Context) : FrameLayout(mContext) {
    private val mView: View
    private val mImageView: TextView
    private val mTouchStartX = 0
    private val mTouchStartY  = 0 //手指按下时坐标
    private lateinit var mParams: WindowManager.LayoutParams
    private val mWindowManager: FloatingManager
    fun show() {
        mParams = WindowManager.LayoutParams()
        mParams.gravity = Gravity.TOP or Gravity.START
        mParams.x = 0
        mParams.y = 100
        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        mParams.width = LayoutParams.WRAP_CONTENT
        mParams.height = LayoutParams.WRAP_CONTENT
        mWindowManager.addView(mView, mParams)
    }

    fun hide() {
        mWindowManager.removeView(mView)
    }

    init {
        val mLayoutInflater = LayoutInflater.from(mContext)
        mView = mLayoutInflater.inflate(R.layout.toast_layout, null)
        mImageView = mView.findViewById(R.id.text1)
        mImageView.setOnClickListener { }
        mWindowManager = getInstance()
    }
}
