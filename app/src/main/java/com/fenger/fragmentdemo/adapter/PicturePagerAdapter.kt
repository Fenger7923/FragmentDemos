package com.fenger.fragmentdemo.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class PicturePagerAdapter(private val images: List<ImageView>, private val viewPager: ViewPager) : PagerAdapter() {

    override fun getCount() = Int.MAX_VALUE // 返回一个无限大的值，确保可以无限循环

    /**
     * 判断是否使用缓存, 如果返回的是true
     * 就使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // 把position对应位置的ImageView添加到ViewPager中
        val iv = images[position % images.size]

        //加上这一段以保证这个iv每次加到viewpager的时候是空着的，不然快速点击确实还是会出问题
        // TODO 确认具体逻辑
        val parent = iv.parent
        if (parent != null) {
            (parent as ViewGroup).removeAllViews()
        }
        // Log.d("fenger","add:"+iv.getId());
        viewPager.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Log.d("fenger","destroy:"+position);
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(images[position % images.size])
    }
}