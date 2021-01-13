package com.fenger.fragmentdemo.Fragments;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fenger.fragmentdemo.NoCopySpanEditableFactory;
import com.fenger.fragmentdemo.Permission.PermissionHelper;
import com.fenger.fragmentdemo.R;
import com.fenger.fragmentdemo.SelectionSpanWatcher;
import com.fenger.fragmentdemo.TestClickableSpan;
import java.io.InputStream;
import java.util.Objects;

import static com.fenger.fragmentdemo.KeyCodeDeleteHelperKt.onDelDown;

/**
 * com.fenger.fragmentdemo.MainFragment
 * Created by fenger
 * in 2020-01-02
 */
public class MainFragment extends Fragment {

    private TextView textView;
    private static final int CODE_CALL_PHONE = 0x0011;

    private SpannableStringBuilder stringBuilder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textView = view.findViewById(R.id.text);
//        // 步骤1：设置需要组合的动画效果
//        ObjectAnimator translation = ObjectAnimator.ofFloat(textView, "translationX", 0, 300, 0);
//        // 平移动画
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(textView, "rotation", 0f, 360f);
//        // 旋转动画
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f, 1f);
//        // 透明度动画
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.play(translation).before(alpha);
//        animSet.start();

        stringBuilder = new SpannableStringBuilder("今天是个好日子，花儿为啥这么红");
        stringBuilder.setSpan(new TestClickableSpan(), 2, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 2, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(stringBuilder);
        textView.setEditableFactory(new NoCopySpanEditableFactory(new SelectionSpanWatcher(new ForegroundColorSpan(Color.RED))));
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onDelDown(((EditText)v).getText());
                }
                return false;
            }
        });
        InputStream in;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//           PermissionHelper.with(getActivity()).requestCode(CODE_CALL_PHONE).requestPermission(new String[] {Manifest.permission.CALL_PHONE}).request();
                PermissionHelper.requestPermission(Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.main_tab),
                        CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA});
                Log.d("fenger", "onClick: 这里是外部的点击事件");
            }
        });
        return view;
    }


    // 圆角图片
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
