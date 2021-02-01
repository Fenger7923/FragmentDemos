package com.fenger.fragmentdemo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fenger.fragmentdemo.adapter.MyFragmentPagerAdapter
import com.fenger.fragmentdemo.customview.ColorTrackTextView
import com.fenger.fragmentdemo.fragments.FindFragment
import com.fenger.fragmentdemo.fragments.LifeFragment
import com.fenger.fragmentdemo.fragments.MainFragment
import com.fenger.fragmentdemo.fragments.MessageFragment
import com.fenger.fragmentdemo.fragments.NewsFragment
import com.fenger.fragmentdemo.permission.PermissionFail
import com.fenger.fragmentdemo.permission.PermissionHelper
import com.fenger.fragmentdemo.permission.PermissionSuccess
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val CODE_CALL_PHONE = 0x0011
    }

    private lateinit var fragments: List<Fragment>
    private lateinit var rb: Array<ColorTrackTextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rb = arrayOf(rb_1, rb_2, rb_3, rb_4, rb_5)

        rb_1.setOnClickListener(this)
        rb_2.setOnClickListener(this)
        rb_3.setOnClickListener(this)
        rb_4.setOnClickListener(this)
        rb_5.setOnClickListener(this)

        initViewPager()
    }

    private fun initViewPager() {
        fragments = listOf(MainFragment(), FindFragment(), NewsFragment(), MessageFragment(), LifeFragment())
        main_tab.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments)

        // 默认一进来加载的页面
        currentItem(2)

        /**
         * 这里有一个问题需要注意
         * 向左滑动的时候，从一开始position就会跳到滑动后的位置，如从2到1，最开始position就变成了1
         * 向右滑动的时候，知道最后一刻position才变成了滑动后的位置。（注意打印的position的值）
         */
        main_tab.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Log.e("fenger", "position --> " + position + " positionOffset --> " + positionOffset);
                if (positionOffset > 0) {
                    // 获取左边
                    val left = rb[position]
                    // 设置朝向
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
                    // 设置进度  positionOffset 是从 0 一直变化到 1 不信可以看打印
                    left.setProgress(1 - positionOffset)

                    // 获取右边
                    val right = rb[position + 1]
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
                    right.setProgress(positionOffset)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Log.d("fenger", "onPageScrollStateChanged: ")
                currentItem(main_tab.currentItem)
            }

            override fun onPageSelected(position: Int) {
                // Log.d("fenger", "onPageSelected: ")
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 在这里处理所有权限是否拿到的问题
        PermissionHelper.onRequestPermissionResult(this, CODE_CALL_PHONE, permissions)
    }

    @PermissionSuccess(requestCode = CODE_CALL_PHONE)
    fun callPhone() {
        Toast.makeText(this, "可以打电话了", Toast.LENGTH_SHORT).show()
    }

    @PermissionFail(requestCode = CODE_CALL_PHONE)
    fun callPhoneFail() {
        Toast.makeText(this, "不可以打电话哟", Toast.LENGTH_SHORT).show()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.rb_1 -> main_tab.currentItem = 0
            R.id.rb_2 -> main_tab.currentItem = 1
            R.id.rb_3 -> main_tab.currentItem = 2
            R.id.rb_4 -> main_tab.currentItem = 3
            R.id.rb_5 -> main_tab.currentItem = 4
        }
    }

    /**
     * 点击某个栏目算作刚进入
     */
    private fun currentItem(item: Int) {
        main_tab.currentItem = item
        //先初始化其他项目
        for (i in rb) {
            i.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
            i.setProgress(0F)
        }

        // 选中的项目变成红色
        rb[item].setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
        rb[item].setProgress(1f)
    }
}
