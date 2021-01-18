package com.fenger.fragmentdemo.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.fenger.fragmentdemo.R

/**
 * com.fenger.passwordedittext
 * Created by fenger
 * in 2019-12-16
 */
class PasswordEditText constructor(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    // 画笔
    private var mPaint: Paint = Paint()

    // 一个密码所占的宽度
    private var mPasswordItemWidth = 0

    // 密码的个数默认为6位数
    private val mPasswordNumber = 6

    // 背景边框颜色
    private var mBgColor = Color.parseColor("#d1d2d6")

    // 背景边框大小
    private var mBgSize = 1

    // 背景边框圆角大小
    private var mBgCorner = 0

    // 分割线的颜色
    private var mDivisionLineColor = mBgColor

    // 分割线的大小
    private var mDivisionLineSize = 1

    // 密码圆点的颜色
    private var mPasswordColor = mDivisionLineColor

    // 密码圆点的半径大小
    private var mPasswordRadius = 4
    private var mListener: PasswordFullListener? = null

    /**
     * 初始化属性
     */
    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)
        // 获取大小
        mDivisionLineSize = array.getDimension(R.styleable.PasswordEditText_divisionLineSize, dip2px(mDivisionLineSize).toFloat()).toInt()
        mPasswordRadius = array.getDimension(R.styleable.PasswordEditText_passwordRadius, dip2px(mPasswordRadius).toFloat()).toInt()
        mBgSize = array.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize).toFloat()).toInt()
        mBgCorner = array.getDimension(R.styleable.PasswordEditText_bgCorner, 0f).toInt()
        // 获取颜色
        mBgColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBgColor)
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor)
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor)
        array.recycle()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
    }

    /**
     * dip 转 px
     */
    private fun dip2px(dip: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), resources.displayMetrics).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        val passwordWidth = width - (mPasswordNumber - 1) * mDivisionLineSize
        mPasswordItemWidth = passwordWidth / mPasswordNumber
        // 绘制背景
        drawBg(canvas)
        // 绘制分割线
        drawDivisionLine(canvas)
        // 绘制密码
        drawHidePassword(canvas)
        if (text?.length ?: 0 >= mPasswordNumber) {
            mListener?.PasswordFull()
        }
    }

    /**
     * 绘制背景
     */
    private fun drawBg(canvas: Canvas) {
        mPaint.color = mBgColor
        // 设置画笔为空心
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBgSize.toFloat()
        val rectF = RectF(mBgSize.toFloat(), mBgSize.toFloat(), (width - mBgSize).toFloat(), (height - mBgSize).toFloat())
        // 如果没有设置圆角，就画矩形
        if (mBgCorner == 0) {
            canvas.drawRect(rectF, mPaint)
        } else {
            // 如果有设置圆角就画圆矩形
            canvas.drawRoundRect(rectF, mBgCorner.toFloat(), mBgCorner.toFloat(), mPaint)
        }
    }

    /**
     * 绘制隐藏的密码
     */
    private fun drawHidePassword(canvas: Canvas) {
        val passwordLength: Int = text?.length ?: 0
        mPaint.color = mPasswordColor
        // 设置画笔为实心
        mPaint.style = Paint.Style.FILL
        for (i in 0 until passwordLength) {
            val cx = i * mDivisionLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize
            canvas.drawCircle(cx.toFloat(), height / 2.toFloat(), mPasswordRadius.toFloat(), mPaint)
        }
    }

    /**
     * 绘制分割线
     */
    private fun drawDivisionLine(canvas: Canvas) {
        mPaint.strokeWidth = mDivisionLineSize.toFloat()
        mPaint.color = mDivisionLineColor
        for (i in 0 until mPasswordNumber - 1) {
            val startX = (i + 1) * mDivisionLineSize + (i + 1) * mPasswordItemWidth + mBgSize
            canvas.drawLine(startX.toFloat(), mBgSize.toFloat(), startX.toFloat(), height - mBgSize.toFloat(), mPaint)
        }
    }

    /**
     * 添加密码
     */
    fun addPassword(number: String) {
        var password = number
        password = text.toString() + password
        if (password.length > mPasswordNumber) {
            return
        }
        setText(password)
    }

    fun setOnPasswordFullListener(listener: PasswordFullListener?) {
        mListener = listener
    }

    /**
     * 删除最后一位密码
     */
    fun deleteLastPassword() {
        var currentText = text.toString()
        if (TextUtils.isEmpty(currentText)) {
            return
        }
        currentText = currentText.substring(0, currentText.length - 1)
        setText(currentText)
    }

    /**
     * 点击键盘的回调监听
     */
    interface PasswordFullListener {
        fun PasswordFull()
    }

    init {
        initPaint()
        initAttributeSet(context, attrs)
        // 设置输入模式是密码-->改为只能输入数字
        inputType = EditorInfo.TYPE_CLASS_NUMBER
        // 不显示光标
        isCursorVisible = false
    }
}