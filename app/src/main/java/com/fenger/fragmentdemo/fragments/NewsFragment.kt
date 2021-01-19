package com.fenger.fragmentdemo.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fenger.fragmentdemo.adapter.PicturePagerAdapter
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.customview.MyViewPager
import com.fenger.fragmentdemo.customview.MyViewPager.ScrollingListener
import java.util.*

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class NewsFragment : Fragment(), ScrollingListener {

    private lateinit var mViewPager: MyViewPager
    private val mImageList: MutableList<ImageView> = ArrayList() //轮播的图片集合
    private lateinit var mTvPagerTitle: TextView //标题
    private var previousPosition = 0 //前一个被选中的position
    private var mDots: List<View> = listOf() //下面的小点
    private lateinit var linearLayoutDots: LinearLayout

    // 在values中创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private val imageIds = intArrayOf(
            R.drawable.pager_image1,
            R.drawable.pager_image2,
            R.drawable.pager_image3,
            R.drawable.pager_image1)

    //标题内容的集合
    private val mImageTitles = arrayOf(
            "这是一个好看的gakki",
            "这是一个优美的gakki",
            "这是一个快乐的gakki",
            "这是一个萌萌的gakki")

    private val times = 2500 //轮播时间

    private val mHandle: Handler =  Handler().apply {
            mViewPager.currentItem = mViewPager.currentItem + 1
            setScroll(times)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater.inflate(R.layout.fragment_news, container, false)
        mViewPager = requireView().findViewById(R.id.mViewPager)
        mTvPagerTitle = requireView().findViewById(R.id.picture_text)
        linearLayoutDots = requireView().findViewById(R.id.point_container)
        init()
        return requireView()
    }

    private fun init() {
        addPic2List()

        //添加下面的小点
        mDots = addDots(linearLayoutDots, fromResToDrawable(context, R.drawable.point_normal), imageIds.size)

        initView() //初始化View，设置适配器
        setFirstLocation() //设置刚打开app时显示的图片和文字
        // setScroll(times); //开始轮播//不在此开始轮播，改为根据是否可见决定是否开始轮播
    }

    private fun initView() {

        val viewPagerAdapter = PicturePagerAdapter(mImageList, mViewPager)
        mViewPager.adapter = viewPagerAdapter
        mViewPager.setScrollingListener(this)

        mViewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                val newPosition = position % imageIds.size
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle.text = mImageTitles[newPosition] //图片下面设置显示文本
                //设置轮播点
                val newDot = mDots[newPosition]
                newDot.background = fromResToDrawable(context, R.drawable.point_pressed)
                val preDot = mDots[previousPosition]
                preDot.background = fromResToDrawable(context, R.drawable.point_normal)

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })
    }

    private fun setFirstLocation() {
        mTvPagerTitle.text = mImageTitles[previousPosition]
        //把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        val m = Int.MAX_VALUE / 2 % imageIds.size
        val currentPosition = Int.MAX_VALUE / 2 - m
        mViewPager.currentItem = currentPosition

        //设置初始的轮播点
        val newDot = mDots[0]
        newDot.background = fromResToDrawable(context, R.drawable.point_pressed)
    }

    /**
     * 资源图片转Drawable
     */
    fun fromResToDrawable(context: Context?, resId: Int): Drawable? {
        return context?.resources?.getDrawable(resId)
    }

    /**
     * 动态添加一个点
     */
    private fun addDot(linearLayout: LinearLayout?, background: Drawable?): Int {
        val dot = View(context)
        val dotParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT)
        dotParams.width = 20
        dotParams.height = 20
        dotParams.setMargins(15, 2, 0, 0)
        dot.layoutParams = dotParams
        dot.background = background
        dot.id = View.generateViewId()
        linearLayout?.addView(dot)
        return dot.id
    }

    /**
     * 添加多个轮播小点到横向线性布局
     */
    private fun addDots(linearLayout: LinearLayout?, background: Drawable?, number: Int): List<View> {
        val dots: MutableList<View> = ArrayList()
        for (i in 0 until number) {
            val dotId = addDot(linearLayout, background)
            dots.add(requireView().findViewById(dotId))
        }
        return dots
    }

    /**
     * 循环播放
     */
    override fun setScroll(time: Int) {
        if (time == 0) {
            mHandle.removeMessages(Companion.MSG)
        } else {
            mHandle.sendEmptyMessageDelayed(Companion.MSG, time.toLong())
        }
    }

    private fun addPic2List() {
        //添加图片到图片列表里
        var iv: ImageView
        for (i in imageIds.indices) {
            iv = ImageView(context)
            iv.setBackgroundResource(imageIds[i]) //设置图片
            iv.id = imageIds[i] //顺便给图片设置id
            mImageList.add(iv)
        }
    }

    /**
     * 在整个FragmentPagerAdapter里面每次可见和不可见变换时都会调用这个方法决定是否可见
     * 在这里使用可见度来决定是否开始轮播，并且避免被外层viewpager影响
     * 每次开始论轮播之前去掉之前的所有轮播（好像可以直接用，不需要把这个可见拿出来干活了）
     *
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        mHandle.removeMessages(Companion.MSG)
        if (isVisibleToUser) {
            setScroll(times)
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    companion object {
        private const val MSG = 0x0011
    }
}
