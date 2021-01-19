package com.fenger.fragmentdemo

import android.content.Context
import android.view.View
import android.view.WindowManager

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020/7/27
 */
internal class FloatingManager private constructor(mContext: Context) {
    private val mWindowManager: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    /**
     * 添加悬浮窗
     *
     * @param view
     * @param params
     * @return
     */
    fun addView(view: View?, params: WindowManager.LayoutParams?): Boolean {
        try {
            mWindowManager.addView(view, params)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 移除悬浮窗
     *
     * @param view
     * @return
     */
    fun removeView(view: View?): Boolean {
        try {
            mWindowManager.removeView(view)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 更新悬浮窗参数
     *
     * @param view
     * @param params
     * @return
     */
    private fun updateView(view: View?, params: WindowManager.LayoutParams?): Boolean {
        try {
            mWindowManager.updateViewLayout(view, params)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    companion object {
        private lateinit var mInstance: FloatingManager
        fun getInstance(): FloatingManager = mInstance
    }
}
