package com.fenger.fragmentdemo.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * com.fenger.myviewpager
 * Created by fenger
 * in 2020-01-02
 */
class LetterSideBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val mPaint: Paint = Paint().apply {
        isAntiAlias = true
        //自定义属性，颜色，大小
        textSize = sp2px(13)
        color = Color.BLUE
    }
    private val mRePaint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = sp2px(12)
        color = Color.RED
    }
    private val mLetters = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")
    private var currentLetter: String? = null //当前触摸的字母

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = paddingLeft + paddingRight + mPaint.measureText("A").toInt()
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val itemHeight = (height - paddingTop - paddingBottom) / mLetters.size
        for (i in mLetters.indices) {
            val x = (width / 2.0 - mPaint.measureText(mLetters[i]) / 2).toInt()
            //中心位置
            val letterCenterY = i * itemHeight + itemHeight / 2 + paddingTop
            //基线
            val dy = ((mPaint.fontMetrics.bottom - mPaint.fontMetrics.top) / 2 - mPaint.fontMetrics.bottom).toInt()
            val baseLine = letterCenterY + dy
            if (mLetters[i] == currentLetter) {
                canvas.drawText(mLetters[i], x.toFloat(), baseLine.toFloat(), mRePaint)
            } else {
                canvas.drawText(mLetters[i], x.toFloat(), baseLine.toFloat(), mPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                Log.e("fenger", "motion")
                //获得当前字母
                val currentMoveY = event.y
                val itemHeight = (height - paddingTop - paddingBottom) / mLetters.size
                var position = (currentMoveY / itemHeight).toInt()
                if (position < 0) {
                    position = 0
                } else if (position > mLetters.size - 1) {
                    position = mLetters.size - 1
                }
                currentLetter = mLetters[position]
                if (mListener != null) {
                    mListener!!.touch(currentLetter, true)
                }

                //重绘
                invalidate()
            }
            MotionEvent.ACTION_UP -> if (mListener != null) {
                mListener!!.touch(currentLetter, false)
            }
        }
        return true
    }

    private fun sp2px(sp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics)
    }

    private var mListener: TouchLetterListener? = null
    fun setMyOnTouchListener(listener: TouchLetterListener?) {
        mListener = listener
    }

    interface TouchLetterListener {
        fun touch(letter: CharSequence?, isTouch: Boolean)
    }
}