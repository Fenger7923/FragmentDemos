package com.fenger.fragmentdemo.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fenger.fragmentdemo.adapter.PicturePagerAdapter;
import com.fenger.fragmentdemo.customview.MyViewPager;
import com.fenger.fragmentdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
public class NewsFragment extends Fragment implements MyViewPager.ScrollingListener {

    private View view;//页面的view

    private MyViewPager mViewPager;
    private List<ImageView> mImageList = new ArrayList<>();//轮播的图片集合
    private TextView mTvPagerTitle;//标题
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//下面的小点
    private LinearLayout linearLayoutDots;

    // 在values中创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] image_ids = new int[]{R.drawable.pager_image1, R.drawable.pager_image2, R.drawable.pager_image3, R.drawable.pager_image1};
    private String[] mImageTitles = new String[]{"这是一个好看的gakki", "这是一个优美的gakki", "这是一个快乐的gakki", "这是一个萌萌的gakki"};//标题内容的集合

    private final int MSG = 0x0011;
    private final int times = 2500;//轮播时间
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            setScroll(times);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        mViewPager = view.findViewById(R.id.mViewPager);
        mTvPagerTitle = view.findViewById(R.id.picture_text);
        linearLayoutDots = view.findViewById(R.id.point_container);
        init();
        return view;
    }

    private void init() {
        initData();//初始化数据
        initView();//初始化View，设置适配器
        setFirstLocation();//设置刚打开app时显示的图片和文字
//        setScroll(times);//开始轮播//不在此开始轮播，改为根据是否可见决定是否开始轮播
    }

    public void initData() {
        addPic2List();

        //添加下面的小点
        mDots = addDots(linearLayoutDots, fromResToDrawable(getContext(), R.drawable.point_normal), image_ids.length);
    }

    public void initView() {
        PicturePagerAdapter viewPagerAdapter = new PicturePagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setScrollingListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % image_ids.length;
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                //设置轮播点
                View newDot = mDots.get(newPosition);
                newDot.setBackground(fromResToDrawable(getContext(), R.drawable.point_pressed));

                View preDot = mDots.get(previousPosition);
                preDot.setBackground(fromResToDrawable(getContext(), R.drawable.point_normal));

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFirstLocation() {
        mTvPagerTitle.setText(mImageTitles[previousPosition]);
        //把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % image_ids.length;
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);

        //设置初始的轮播点
        View newDot = mDots.get(0);
        newDot.setBackground(fromResToDrawable(getContext(), R.drawable.point_pressed));

    }

    /**
     * 资源图片转Drawable
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 动态添加一个点
     */
    private int addDot(final LinearLayout linearLayout, Drawable background) {
        final View dot = new View(getContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 20;
        dotParams.height = 20;
        dotParams.setMargins(15, 2, 0, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(background);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    /**
     * 添加多个轮播小点到横向线性布局
     */
    public List<View> addDots(final LinearLayout linearLayout, Drawable background, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, background);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }

    /**
     * 循环播放
     */
    @Override
    public void setScroll(int time) {
        if (time == 0) {
            mHandle.removeMessages(MSG);
        } else {
            mHandle.sendEmptyMessageDelayed(MSG, time);
        }
    }

    private void addPic2List() {
        //添加图片到图片列表里
        ImageView iv;
        for (int i = 0; i < image_ids.length; i++) {
            iv = new ImageView(getContext());
            iv.setBackgroundResource(image_ids[i]);//设置图片
            iv.setId(image_ids[i]);//顺便给图片设置id
            mImageList.add(iv);
        }
    }

    /**
     * 在整个FragmentPagerAdapter里面每次可见和不可见变换时都会调用这个方法决定是否可见
     * 在这里使用可见度来决定是否开始轮播，并且避免被外层viewpager影响
     * 每次开始论轮播之前去掉之前的所有轮播（好像可以直接用，不需要把这个可见拿出来干活了）
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mHandle.removeMessages(MSG);
        if (isVisibleToUser) {
            setScroll(times);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
