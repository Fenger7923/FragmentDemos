package com.fenger.fragmentdemo.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.customview.StepView
import com.fenger.fragmentdemo.spanutils.NotificationUtils

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class MessageFragment : Fragment() {
    private lateinit var stepView: StepView
    private lateinit var valueAnimator: ValueAnimator
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        stepView = view.findViewById(R.id.step_view)
        stepView.setProgress(10000)

        //属性动画
        valueAnimator = ObjectAnimator.ofFloat(0f, 3777f)
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener{ animation ->
            val current = animation.animatedValue as Float
            stepView.setCurrent(current.toInt())
        }
        valueAnimator.start()

        stepView.setOnClickListener {
            NotificationUtils.showNotification("内容", 0, 0, 0)
        }

        return view
    }
}
