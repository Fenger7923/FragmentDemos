package com.fenger.fragmentdemo.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.fenger.fragmentdemo.R

class ColorTrackTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private lateinit var mOriginPaint: Paint // 画不变色的字体
    private lateinit var mChangePaint: Paint // 画变色的字体
    private var mProgress = 0.0f // 绘制进度

    //默认方向是反向变色
    private var mDirection: Direction = Direction.LEFT_TO_RIGHT

    enum class Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    init {
        initTypeArray(context, attrs)
    }

    private fun initTypeArray(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView)
        val originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, textColors.defaultColor)
        val changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, textColors.defaultColor)
        mOriginPaint = getPaintByColor(originColor)
        mChangePaint = getPaintByColor(changeColor)
        array.recycle()
    }

    private fun getPaintByColor(color: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.isAntiAlias = true // 抗锯齿
        paint.isDither = true // 防抖动
        paint.textSize = textSize
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        // canvas.clipRect() // 裁剪区域
        // 思路：利用裁剪区域不断改变两边的画笔，达到功能
        // super.onDraw(canvas) // 这里不能调用系统的onDraw了
        val middle: Int = (mProgress * width).toInt()
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, mOriginPaint, 0, middle)
            drawText(canvas, mChangePaint, middle, width)
        } else {
            drawText(canvas, mOriginPaint, width - middle, width)
            drawText(canvas, mChangePaint, 0, width - middle)
        }
    }

    private fun drawText(canvas: Canvas, paint: Paint, startPosition: Int, endPosition: Int) {
        canvas.save() // 保存画布
        val rect = Rect(startPosition, 0, endPosition, height)
        canvas.clipRect(rect)
        val text = text.toString()
        // 获取字体宽度
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val dx = width / 2 - bounds.width() / 2
        // 基线
        val fontMetricsInt = paint.fontMetricsInt
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        val baseLine = height / 2 + dy
        canvas.drawText(text, dx.toFloat(), baseLine.toFloat(), paint)
        canvas.restore()
    }

    fun setDirection(direction: Direction) {
        mDirection = direction
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }
}
