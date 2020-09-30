package com.fenger.fragmentdemo.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.fenger.fragmentdemo.R;

/**
 * com.fenger.myviewpager
 * Created by fenger
 * in 2019-12-26
 */
public class ColorTrackTextView extends AppCompatTextView {

    private Paint mOriginPaint;//画不变色的字体
    private Paint mChangePaint;//画变色的字体
    private float mProgress = 0.0f; //绘制进度'

    //默认方向是反向变色
    private Direction mDirection = Direction.Left_TO_RIGHT;

    public void setChangeColor(int color) {
        this.mChangePaint.setColor(color);
    }

    public void setOriginColor(int color) {
        this.mOriginPaint.setColor(color);
    }

    public enum Direction {
        Left_TO_RIGHT, RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeArray(context, attrs);
    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());
        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);
        array.recycle();
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }

    //一个文字两种颜色

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas.clipRect();//裁剪区域
        //思路：利用裁剪区域不断改变两边的画笔，达到功能
        //super.onDraw(canvas);//这里不能调用系统的onDraw了
        int middle = (int) (mProgress * getWidth());
        if (mDirection == Direction.Left_TO_RIGHT) {
            drawText(canvas, mOriginPaint, 0, middle);
            drawText(canvas, mChangePaint, middle, getWidth());
        } else {
            drawText(canvas, mOriginPaint, getWidth() - middle, getWidth());
            drawText(canvas, mChangePaint, 0, getWidth() - middle);
        }
    }

    private void drawText(Canvas canvas, Paint paint, int startPosition, int endPosition) {
        canvas.save();//保存画布

        Rect rect = new Rect(startPosition, 0, endPosition, getHeight());
        canvas.clipRect(rect);
        String text = getText().toString();
        //获取字体宽度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int dx = getWidth() / 2 - bounds.width() / 2;
        //基线
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, paint);
        canvas.restore();
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}