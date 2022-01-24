package com.fenger.fragmentdemo.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author fengerzhang
 * @date 2022/1/23 15:10
 */
class CanvasView: View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val number =  -300..1000

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val xyLine = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val demoPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(200f,1500f)
        canvas.scale(1f,-1f)

        canvas.drawLine(-10000f, 0f, 10000f, 0f, xyLine)
        canvas.drawLine(0f,-10000f,  0f,10000f,  xyLine)
        val pointList = arrayListOf<PointF>()
        number.forEach { t ->
            val point = PointF()
            if (t % 2 == 0) {
                point.x = t.toFloat()
                point.y = 2f * t - 80
                pointList.add(point)
                canvas.drawPoint(point.x, point.y, demoPaint)
            }
        }
        number.forEach { t ->
            val point = PointF()
            val pointDown = PointF()
            point.x = t.toFloat()
            pointDown.x = t.toFloat()
            point.y = sqrt(160.0.pow(2.0).toFloat() - ((point.x - 10).toDouble()).pow(2.0)).toFloat() + 10
            pointDown.y = -sqrt(160.0.pow(2.0).toFloat() - ((pointDown.x - 10).toDouble()).pow(2.0)).toFloat() + 10
            canvas.drawPoint(point.x, point.y, demoPaint)
            canvas.drawPoint(pointDown.x, pointDown.y, demoPaint)
        }

        drawQuz(canvas)
    }

    private var moveX: Float = 160f
    private var moveY: Float = 160f
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE -> {
                //在控制点附近范围内部,进行移动
                Log.e("x=", "onTouchEvent: (x,y)"+(event.x - width / 2).toInt()+":"+(-(event.y - height / 2)).toInt())
                //将手势坐标转换为屏幕坐标
                moveX = event.x - width / 2
                moveY = -(event.y - height / 2)
                invalidate()
            }
        }
        return true
    }

    private fun drawQuz(canvas: Canvas) {
        canvas.drawCircle(0f, 0f, 10f, paint)
        canvas.drawCircle(400f, 0f, 10f, paint)
        //第一个点和控制点的连线到最后一个点链线。为了方便观察
        val lineLeft = Path().apply {
            moveTo(0f, 0f)
            lineTo(moveX, moveY)
            lineTo(400f, 0f)
        }
        canvas.drawPath(lineLeft, paint)
        //第一个p0处画一个圆。第二个p1处画一个控制点圆,最后画一个。
        canvas.drawCircle(moveX, moveY, 10f, paint)
        val quePath = Path()
        quePath.quadTo(moveX, moveY, 400f, 0f)
        canvas.drawPath(quePath, paint)
    }
}