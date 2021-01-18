package com.fenger.fragmentdemo.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fenger.fragmentdemo.customview.StepView;
import com.fenger.fragmentdemo.R;

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
public class MessageFragment extends Fragment {

    private StepView stepView;
    private ValueAnimator valueAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        stepView = view.findViewById(R.id.step_view);
        stepView.setProgress(10000);

        //属性动画
        valueAnimator = ObjectAnimator.ofFloat(0, 3777);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (float) animation.getAnimatedValue();
                stepView.setCurrent((int) current);
            }
        });
        valueAnimator.start();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //由于这个页面加载之前就会走这个方法，所以需要做空值处理
            try {
                valueAnimator.start();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
