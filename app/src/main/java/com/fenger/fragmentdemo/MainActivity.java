package com.fenger.fragmentdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fenger.fragmentdemo.Adapter.MyFragmentPagerAdapter;
import com.fenger.fragmentdemo.CustomView.ColorTrackTextView;
import com.fenger.fragmentdemo.CustomView.MyViewPager;
import com.fenger.fragmentdemo.Fragments.FindFragment;
import com.fenger.fragmentdemo.Fragments.LifeFragment;
import com.fenger.fragmentdemo.Fragments.MainFragment;
import com.fenger.fragmentdemo.Fragments.MessageFragment;
import com.fenger.fragmentdemo.Fragments.NewsFragment;
import com.fenger.fragmentdemo.Permission.PermissionFail;
import com.fenger.fragmentdemo.Permission.PermissionHelper;
import com.fenger.fragmentdemo.Permission.PermissionSuccess;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ColorTrackTextView rb_1, rb_2, rb_3, rb_4, rb_5;
    ColorTrackTextView[] rb;
    private MyViewPager mPager;
    private List<Fragment> fragments;
    private static final int CODE_CALL_PHONE = 0x0011;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        rb_1.setOnClickListener(this);
        rb_2.setOnClickListener(this);
        rb_3.setOnClickListener(this);
        rb_4.setOnClickListener(this);
        rb_5.setOnClickListener(this);
        mPager = findViewById(R.id.main_tab);
        rb = new ColorTrackTextView[]{rb_1, rb_2, rb_3, rb_4, rb_5};

        button = findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView floatingView = new FloatingView(MainActivity.this);
                floatingView.show();
            }
        });
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new FindFragment());
        fragments.add(new NewsFragment());
        fragments.add(new MessageFragment());
        fragments.add(new LifeFragment());
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));

        //默认一进来加载的页面
        currentItem(0);
        /**
         * 这里有一个问题需要注意
         * 向左滑动的时候，从一开始position就会跳到滑动后的位置，如从2到1，最开始position就变成了1
         * 向右滑动的时候，知道最后一刻position才变成了滑动后的位置。（注意打印的position的值）
         */
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("fenger", "position --> " + position + " positionOffset --> " + positionOffset);
                if (positionOffset > 0) {
                    // 获取左边
                    ColorTrackTextView left = rb[position];
                    // 设置朝向
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    // 设置进度  positionOffset 是从 0 一直变化到 1 不信可以看打印
                    left.setProgress(1 - positionOffset);

                    // 获取右边
                    ColorTrackTextView right = rb[position + 1];
                    right.setDirection(ColorTrackTextView.Direction.Left_TO_RIGHT);
                    right.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("fenger", "onPageSelected: ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d("fenger", "onPageScrollStateChanged: ");
                currentItem(mPager.getCurrentItem());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_1:
                mPager.setCurrentItem(0);
                break;
            case R.id.rb_2:
                mPager.setCurrentItem(1);
                break;
            case R.id.rb_3:
                mPager.setCurrentItem(2);
                break;
            case R.id.rb_4:
                mPager.setCurrentItem(3);
                break;
            case R.id.rb_5:
                mPager.setCurrentItem(4);
                break;
        }
    }

    /**
     * 处理权限之后会回调这个方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //在这里处理所有权限是否拿到的问题
        PermissionHelper.onRequestPermissionResult(this, CODE_CALL_PHONE, permissions);
    }

    @PermissionSuccess(requestCode = CODE_CALL_PHONE)
    public void callPhone() {
        Toast.makeText(this, "可以打电话了", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = CODE_CALL_PHONE)
    public void callPhoneFail() {
        Toast.makeText(this, "不可以打电话哟", Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击某个栏目算作刚进入
     */
    private void currentItem(int item) {
        mPager.setCurrentItem(item);
        //先初始化其他项目
        for (ColorTrackTextView i : rb) {
            i.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
            i.setProgress(0);
        }

        //选中的项目变成红色
        rb[item].setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
        rb[item].setProgress(1);
    }
}
