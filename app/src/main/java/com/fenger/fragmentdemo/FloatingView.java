package com.fenger.fragmentdemo;

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020/7/27
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fenger.fragmentdemo.Fragments.MainFragment;

/**
 * 悬浮窗view
 */
public class FloatingView extends FrameLayout {
    private Context mContext;
    private View mView;
    private TextView mImageView;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;

    public FloatingView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.toast_layout, null);
        mImageView =  mView.findViewById(R.id.text1);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mWindowManager = FloatingManager.getInstance(mContext);
    }

    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 100;
        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }
}
