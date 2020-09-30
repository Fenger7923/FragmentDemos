package com.fenger.fragmentdemo.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fenger.fragmentdemo.R;

import java.util.List;

/**
 * com.fenger.fragmentdemo.Adapter
 * Created by fenger
 * in 2020-01-06
 */
public class PicturePagerAdapter extends PagerAdapter {

    private List<ImageView> images;
    private ViewPager viewPager;
    // 在values文件夹下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] image_ids = new int[]{R.drawable.pager_image1, R.drawable.pager_image2, R.drawable.pager_image3};

    public PicturePagerAdapter(List<ImageView> images, ViewPager viewPager) {
        this.images = images;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回一个无限大的值，可以 无限循环
    }

    /**
     * 判断是否使用缓存, 如果返回的是true
     * 就使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化一个条目
     *
     * @param container
     * @param position  当前需要加载条目的索引
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中
        ImageView iv = images.get(position % images.size());

        //加上这一段以保证这个iv每次加到viewpager的时候是空着的，不然快速点击确实还是会出问题
        ViewGroup parent = (ViewGroup) iv.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

//        Log.d("fenger","add:"+iv.getId());
        viewPager.addView(iv);
        // 把当前添加ImageView返回回去.
        return iv;
    }

    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        Log.d("fenger","destroy:"+position);
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(images.get(position % images.size()));
    }
}
