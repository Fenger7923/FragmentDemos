package com.fenger.fragmentdemo.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.adapter.PicturePagerAdapter
import com.fenger.fragmentdemo.customview.MyViewPager
import com.fenger.fragmentdemo.customview.MyViewPager.ScrollingListener

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class NewsFragment : Fragment(), ScrollingListener {

    private var mViewPager: MyViewPager? = null
    private val mImageList: MutableList<ImageView> = ArrayList() //轮播的图片集合
    private var mTvPagerTitle: TextView? = null //标题
    private var previousPosition = 0 //前一个被选中的position
    private var mDots: List<View>? = null //下面的小点
    private lateinit var linearLayoutDots: LinearLayout

    // 在values中创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private val imageIds = intArrayOf(
        R.drawable.pager_image1,
        R.drawable.pager_image2,
        R.drawable.pager_image3,
        R.drawable.pager_image1
    )

    //标题内容的集合
    private val mImageTitles = arrayOf(
        "这是一个好看的gakki",
        "这是一个优美的gakki",
        "这是一个快乐的gakki",
        "这是一个萌萌的gakki"
    )

    private val times = 2500 //轮播时间

    private val handler: Handler = Handler {
        Log.d("fenger", "handler start")
        if (mViewPager != null) {
            mViewPager!!.currentItem = mViewPager!!.currentItem + 1
            setScroll(times)
        }
        return@Handler true
    }

    companion object {
        private const val MSG = 0x0011
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        mViewPager = view.findViewById(R.id.mViewPager)
        mTvPagerTitle = view.findViewById(R.id.picture_text)
        linearLayoutDots = view.findViewById(R.id.point_container)
        init()
        return view
    }

    private fun init() {
        addPic2List()

//        添加下面的小点
        mDots = addDots(linearLayoutDots, fromResToDrawable(R.drawable.point_normal), imageIds.size)

        initView() //初始化View，设置适配器
        setFirstLocation() //设置刚打开app时显示的图片和文字
        setScroll(times) //开始轮播//不在此开始轮播，改为根据是否可见决定是否开始轮播
    }

    private fun initView() {

        val viewPagerAdapter = mViewPager?.let { PicturePagerAdapter(mImageList, it) }
        mViewPager?.adapter = viewPagerAdapter
        mViewPager?.setScrollingListener(this)

        mViewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                val newPosition = position % imageIds.size
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle?.text = mImageTitles[newPosition] //图片下面设置显示文本
                //设置轮播点
                val newDot = mDots?.get(newPosition)
                newDot?.background = fromResToDrawable(R.drawable.point_pressed)
                val preDot = mDots?.get(previousPosition)
                preDot?.background = fromResToDrawable(R.drawable.point_normal)

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition
            }
        })
    }

    private fun setFirstLocation() {
        mTvPagerTitle?.text = mImageTitles[previousPosition]
        //把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        val currentPosition = Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % imageIds.size
        mViewPager?.currentItem = currentPosition

        //设置初始的轮播点
        val newDot = mDots?.get(0)
        newDot?.background = fromResToDrawable(R.drawable.point_pressed)
    }

    /**
     * 资源图片转Drawable
     */
    private fun fromResToDrawable(resId: Int): Drawable? {
        return ResourcesCompat.getDrawable(resources, resId, null)
    }

    /**
     * 动态添加一个点
     */
    private fun addDot(linearLayout: LinearLayout, drawable: Drawable?): View {
        val dotParams = LinearLayout.LayoutParams(20, 20).apply {
            width = 20
            height = 20
            setMargins(20, 5, 0, 0)
        }
        val dot = View(context).apply {
            layoutParams = dotParams
            background = drawable
            id = View.generateViewId()
        }
        linearLayout.addView(dot)
        return dot
    }

    /**
     * 添加多个轮播小点到横向线性布局
     */
    private fun addDots(
        linearLayout: LinearLayout,
        background: Drawable?,
        number: Int
    ): List<View> {
        val dots: ArrayList<View> = ArrayList()
        for (i in 0 until number) {
            val dotId = addDot(linearLayout, background)
            dots.add(dotId)
        }
        return dots
    }

    /**
     * 循环播放
     */
    override fun setScroll(scrollTime: Int) {
        if (scrollTime == 0) {
            handler.removeMessages(MSG)
        } else {
            handler.sendEmptyMessageDelayed(MSG, scrollTime.toLong())
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
}
