package com.fenger.fragmentdemo.customview

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.fenger.fragmentdemo.R

/**
 * com.fenger.passwordedittext
 * Created by fenger
 * in 2019-12-17
 */
class CustomerKeyboard(context: Context) : LinearLayout(context), View.OnClickListener {
    private var mListener: CustomerKeyboardClickListener? = null

    /**
     * 设置键盘子View的点击事件
     */
    private fun setChildViewOnclick(parent: ViewGroup) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            // 不断的递归设置点击事件
            val view = parent.getChildAt(i)
            //递归点击事件===如果是个父控件就去递归内部的子控件，直到最后view控件
            if (view is ViewGroup) {
                setChildViewOnclick(view)
                continue
            }
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        if (v is Button) {
            // 如果是图片那肯定点击的是删除
            mListener?.delete()
        } else if (v is TextView) {
            // 如果点击的是TextView
            val number = v.text.toString()
            if (!TextUtils.isEmpty(number)) {
                // 回调
                mListener?.click(number)
            }
        }
    }

    /**
     * 设置键盘的点击回调监听
     */
    fun setOnCustomerKeyboardClickListener(listener: CustomerKeyboardClickListener?) {
        mListener = listener
    }

    /**
     * 点击键盘的回调监听
     */
    interface CustomerKeyboardClickListener {
        fun click(number: String)
        fun delete()
    }

    init {
        View.inflate(context, R.layout.ui_customer_keyboard, this)
        setChildViewOnclick(this)
    }
}