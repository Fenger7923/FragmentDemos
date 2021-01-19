package com.fenger.fragmentdemo

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020/5/25
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Log.d("fenger", "onCreate: ")
    }

    companion object {
        private var mInstance: MyApplication? = null
        val instance: Context?
            get() = mInstance
    }
}
