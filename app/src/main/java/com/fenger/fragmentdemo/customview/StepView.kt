package com.fenger.fragmentdemo.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.fenger.fragmentdemo.R
import android.graphics.RectF as RectF1

/**
 * com.fenger.stepview
 * Created by fenger
 * in 2019-12-20
 */
class StepView constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mOuterColor = Color.RED
    private var mInnerColor = Color.BLUE
    private var mBorderWidth = 20 //20px,但是需要给的应该是dip
    private var mStepTextSize: Int = 30
    private var mStepTextColor = Color.RED
    private val mPaint: Paint
    private val mInnerPaint: Paint
    private val mTextPaint: Paint
    private var mProgress = 0
    private var mCurrent = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取模式  不允许AT_MOST

        //获取输入的值
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        //调用设置宽高的方法
        //setMeasuredDimension(width,height);
        //这里我们选择绘制一个正方形，所以取宽高里面的最小值作为整个view的宽高
        setMeasuredDimension(if (width > height) height else width, if (width > height) height else width)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val begin = mBorderWidth / 2 .toFloat()//画笔起始
        val end = width - mBorderWidth / 2 .toFloat()//画笔终结处
        val rectF = RectF1(begin, begin, end, end) //给个区域//要计算画笔的宽度
        canvas.drawArc(rectF, 135f, 270f, false, mPaint) //画半圆弧//从135度开始扫过270度，计算方式自己想去//
        if (mCurrent == 0) {
            return
        }
        val angle = mCurrent.toFloat() / mProgress * 270
        canvas.drawArc(rectF, 135f, angle, false, mInnerPaint)

        //文字，文字开始位置，文字结束位置
        val text = mCurrent.toString() + ""
        val textBound = Rect()
        mTextPaint.getTextBounds(text, 0, text.length, textBound)
        val dx = width / 2 - textBound.width() / 2
        //基线
        val fontMetricsInt = mTextPaint.fontMetricsInt
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        val baseLine = height / 2 + dy
        canvas.drawText(text, dx.toFloat(), baseLine.toFloat(), mTextPaint)
    }

    //动画效果不在view里面写
    //数据获取
    fun setProgress(progress: Int): Int {
        mProgress = progress
        return mProgress
    }

    @Synchronized
    fun setCurrent(current: Int) {
        mCurrent = current
        //不断绘制
        invalidate()
    }

    /**
     * 三个构造方法
     * 1.只有一个参数的构造方法：一般是在java代码创建视图的时候被调用，如果是从xml填充的视图，就不会调用这个.
     * 2.有两个参数的构造方法：这个是在xml创建但是没有指定style的时候被调用。
     * 3.三个参数的构造方法:这个就很容易理解了，是有指定的style的时候被调用。
     */
    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.StepView)
        mOuterColor = array.getColor(R.styleable.StepView_outerColor, mOuterColor)
        mInnerColor = array.getColor(R.styleable.StepView_innerColor, mInnerColor)
        mBorderWidth = array.getDimension(R.styleable.StepView_borderWidth, mBorderWidth.toFloat()).toInt()
        mStepTextSize = array.getDimension(R.styleable.StepView_stepTextSize, mStepTextSize.toFloat()).toInt()
        mStepTextColor = array.getColor(R.styleable.StepView_stepTextColor, mStepTextColor)
        array.recycle() //每次，array用完必须释放一下
        mPaint = Paint()
        mPaint.isAntiAlias = true //画笔抗锯齿
        mPaint.strokeWidth = mBorderWidth.toFloat() //画笔宽度//注意：画笔中心点在宽度中间而不是两边
        mPaint.color = mOuterColor //画笔颜色
        mPaint.style = Paint.Style.STROKE //实心画笔
        mPaint.strokeCap = Paint.Cap.ROUND //开始是个圆弧
        mInnerPaint = Paint()
        mInnerPaint.isAntiAlias = true //画笔抗锯齿
        mInnerPaint.strokeWidth = mBorderWidth.toFloat() //画笔宽度//注意：画笔中心点在宽度中间而不是两边
        mInnerPaint.color = mInnerColor //画笔颜色
        mInnerPaint.style = Paint.Style.STROKE //实心画笔
        mInnerPaint.strokeCap = Paint.Cap.ROUND //开始是个圆弧
        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.color = mStepTextColor
        mTextPaint.textSize = mStepTextSize.toFloat()
    }
}