package com.fenger.fragmentdemo.CustomView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenger.fragmentdemo.R;

/**
 * com.fenger.passwordedittext
 * Created by fenger
 * in 2019-12-17
 */
public class CustomerKeyboard extends LinearLayout implements View.OnClickListener {

    private CustomerKeyboardClickListener mListener;

    public CustomerKeyboard(Context context) {
        this(context, null);
    }

    public CustomerKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.ui_customer_keyboard, this);
        setChildViewOnclick(this);
    }


    /**
     * 设置键盘子View的点击事件
     */
    private void setChildViewOnclick(ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            // 不断的递归设置点击事件
            View view = parent.getChildAt(i);
            //递归点击事件===如果是个父控件就去递归内部的子控件，直到最后view控件
            if (view instanceof ViewGroup) {
                setChildViewOnclick((ViewGroup) view);
                continue;
            }
            view.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        View clickView = v;
        if (clickView instanceof Button) {
            // 如果是图片那肯定点击的是删除
            if (mListener != null) {
                mListener.delete();
            }
        } else if (clickView instanceof TextView) {
            // 如果点击的是TextView
            String number = ((TextView) clickView).getText().toString();
            if (!TextUtils.isEmpty(number)) {
                if (mListener != null) {
                    // 回调
                    mListener.click(number);
                }
            }
        }
    }

    /**
     * 设置键盘的点击回调监听
     */
    public void setOnCustomerKeyboardClickListener(CustomerKeyboardClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击键盘的回调监听
     */
    public interface CustomerKeyboardClickListener {
        public void click(String number);

        public void delete();
    }
}
