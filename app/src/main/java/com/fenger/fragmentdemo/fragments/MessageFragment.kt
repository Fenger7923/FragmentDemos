package com.fenger.fragmentdemo.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.customview.StepView

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class MessageFragment : Fragment() {
    private lateinit var stepView: StepView
    private var valueAnimator: ValueAnimator? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        stepView = view.findViewById(R.id.step_view)
        stepView.setProgress(10000)

        //属性动画
        valueAnimator = ObjectAnimator.ofFloat(0f, 3777f)
        valueAnimator?.duration = 1000
        valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val current = animation.animatedValue as Float
            stepView.setCurrent(current.toInt())
        })
        valueAnimator?.start()

        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            //由于这个页面加载之前就会走这个方法，所以需要做空值处理
            valueAnimator?.start()
        }
        super.setUserVisibleHint(isVisibleToUser)
    }
}
