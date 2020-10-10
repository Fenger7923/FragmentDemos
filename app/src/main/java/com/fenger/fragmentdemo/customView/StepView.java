package com.fenger.fragmentdemo.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fenger.fragmentdemo.R;

/**
 * com.fenger.stepview
 * Created by fenger
 * in 2019-12-20
 */
public class StepView extends View {
    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20;//20px,但是需要给的应该是dip
    private int mStepTextSize;
    private int mStepTextColor = Color.RED;
    private Paint mPaint, mInnerPaint, mTextPaint;

    private int mProgress;
    private int mCurrent;

    /**
     * 三个构造方法
     * 1.只有一个参数的构造方法：一般是在java代码创建视图的时候被调用，如果是从xml填充的视图，就不会调用这个.
     * 2.有两个参数的构造方法：这个是在xml创建但是没有指定style的时候被调用。
     * 3.三个参数的构造方法:这个就很容易理解了，是有指定的style的时候被调用。
     */

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOuterColor = array.getColor(R.styleable.StepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.StepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.StepView_borderWidth, mBorderWidth);
        mStepTextSize = (int) array.getDimension(R.styleable.StepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.StepView_stepTextColor, mStepTextColor);
        array.recycle();//每次，array用完必须释放一下

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//画笔抗锯齿
        mPaint.setStrokeWidth(mBorderWidth);//画笔宽度//注意：画笔中心点在宽度中间而不是两边
        mPaint.setColor(mOuterColor);//画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);//实心画笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);//开始是个圆弧

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);//画笔抗锯齿
        mInnerPaint.setStrokeWidth(mBorderWidth);//画笔宽度//注意：画笔中心点在宽度中间而不是两边
        mInnerPaint.setColor(mInnerColor);//画笔颜色
        mInnerPaint.setStyle(Paint.Style.STROKE);//实心画笔
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);//开始是个圆弧

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取模式  不允许AT_MOST

        //获取输入的值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //调用设置宽高的方法
        //setMeasuredDimension(width,height);
        //这里我们选择绘制一个正方形，所以取宽高里面的最小值作为整个view的宽高
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int begin = mBorderWidth / 2;//画笔起始
        int end = getWidth() - mBorderWidth / 2;//画笔终结处
        RectF rectF = new RectF(begin, begin, end, end);//给个区域//要计算画笔的宽度
        canvas.drawArc(rectF, 135, 270, false, mPaint);//画半圆弧//从135度开始扫过270度，计算方式自己想去//

        if (mCurrent == 0) {
            return;
        }
        float angle = (float) mCurrent / mProgress * 270;
        canvas.drawArc(rectF, 135, angle, false, mInnerPaint);

        //文字，文字开始位置，文字结束位置

        String text = mCurrent + "";
        Rect textBound = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBound);
        int dx = getWidth() / 2 - textBound.width() / 2;
        //基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, mTextPaint);
    }

    //动画效果不在view里面写
    //数据获取
    public int setProgress(int progress) {
        this.mProgress = progress;
        return mProgress;
    }

    public synchronized void setCurrent(int current) {
        this.mCurrent = current;
        //不断绘制
        invalidate();
    }
}
