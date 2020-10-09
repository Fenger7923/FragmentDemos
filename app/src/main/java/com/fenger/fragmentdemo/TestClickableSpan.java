package com.fenger.fragmentdemo;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

public class TestClickableSpan extends ClickableSpan {
    @Override
    public void onClick(@NonNull View widget) {
        Log.d("TestClickableSpan", "onClick: 这里是内部的点击事件");
    }
}
