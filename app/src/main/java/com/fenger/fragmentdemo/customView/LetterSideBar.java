package com.fenger.fragmentdemo.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * com.fenger.myviewpager
 * Created by fenger
 * in 2020-01-02
 */
public class LetterSideBar extends View {

    private Paint mPaint;
    private Paint mRePaint;
    private String currentLetter;//当前触摸的字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //自定义属性，颜色，大小
        mPaint.setTextSize(sp2px(12));
        mPaint.setColor(Color.BLUE);

        mRePaint = new Paint();
        mRePaint.setAntiAlias(true);
        mRePaint.setTextSize(sp2px(12));
        mRePaint.setColor(Color.RED);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getPaddingLeft() + getPaddingRight() + (int) mPaint.measureText("A");
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;

        for (int i = 0; i < mLetters.length; i++) {
            int x = (int) (getWidth() / 2 - mPaint.measureText(mLetters[i]) / 2);
            //中心位置
            int lettercenterY = i * itemHeight + itemHeight / 2 + getPaddingTop();
            //基线
            int dy = (int) ((mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) / 2 - mPaint.getFontMetrics().bottom);
            int baseLine = lettercenterY + dy;


            if (mLetters[i].equals(currentLetter)) {
                canvas.drawText(mLetters[i], x, baseLine, mRePaint);
            } else {
                canvas.drawText(mLetters[i], x, baseLine, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                Log.e("fenger", "motion");
                //获得当前字母
                float currentMoveY = event.getY();
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int position = (int) (currentMoveY / itemHeight);
                if (position < 0) {
                    position = 0;
                } else if (position > mLetters.length - 1) {
                    position = mLetters.length - 1;
                }
                currentLetter = mLetters[position];

                if (mListener != null) {
                    mListener.touch(currentLetter, true);
                }

                //重绘
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.touch(currentLetter, false);
                }
                break;
            default:
                break;
        }

        return true;
    }

    private TouchLetterListener mListener;

    public void setMyOnTouchListener(TouchLetterListener listener) {
        this.mListener = listener;
    }

    public interface TouchLetterListener {
        void touch(CharSequence letter, boolean isTouch);
    }
}