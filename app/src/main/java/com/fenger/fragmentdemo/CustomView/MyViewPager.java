package com.fenger.fragmentdemo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020-01-06
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mListener != null) {
                    mListener.setScroll(0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.setScroll(2500);
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    private ScrollingListener mListener;

    public void setScrollingListener(ScrollingListener listener) {
        this.mListener = listener;
    }

    public interface ScrollingListener {
        void setScroll(int scrollTime);
    }

}
