package com.fenger.fragmentdemo.Fragments;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fenger.fragmentdemo.Permission.PermissionHelper;
import com.fenger.fragmentdemo.R;

/**
 * com.fenger.fragmentdemo.MainFragment
 * Created by fenger
 * in 2020-01-02
 */
public class MainFragment extends Fragment {

    private TextView textView;
    private static final int CODE_CALL_PHONE = 0x0011;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textView = view.findViewById(R.id.text);
        // 步骤1：设置需要组合的动画效果
        ObjectAnimator translation = ObjectAnimator.ofFloat(textView, "translationX", 0, 300, 0);
        // 平移动画
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textView, "rotation", 0f, 360f);
        // 旋转动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f, 1f);
        // 透明度动画
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(translation).before(alpha);
        animSet.start();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//           PermissionHelper.with(getActivity()).requestCode(CODE_CALL_PHONE).requestPermission(new String[] {Manifest.permission.CALL_PHONE}).request();
                PermissionHelper.requestPermission(getFragmentManager().findFragmentById(R.id.main_tab), CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA});
            }
        });
        return view;
    }


    public Bitmap getRoundRectBitmap(Bitmap bitmap, int radius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        final RectF rectF = new RectF(0, 0, 500, 500);

        Canvas canvas = new Canvas(bitmap);

        paint.setXfermode(null);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        return bitmap;
    }
}
