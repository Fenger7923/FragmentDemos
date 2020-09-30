package com.fenger.fragmentdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * com.fenger.fragmentdemo
 * Created by fenger
 * in 2020/5/25
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Log.d("fenger", "onCreate: ");
    }

    public static Context getInstance() {
        return mInstance;
    }
}
