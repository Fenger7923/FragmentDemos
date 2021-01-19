package com.fenger.fragmentdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fenger.fragmentdemo.R
import com.fenger.fragmentdemo.customview.CustomerKeyboard
import com.fenger.fragmentdemo.customview.CustomerKeyboard.CustomerKeyboardClickListener
import com.fenger.fragmentdemo.customview.PasswordEditText
import com.fenger.fragmentdemo.customview.PasswordEditText.PasswordFullListener

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
class LifeFragment : Fragment(), CustomerKeyboardClickListener, PasswordFullListener {

    private lateinit var mCustomerKeyboard: CustomerKeyboard
    private lateinit var mPasswordEditText: PasswordEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_life, container, false)
        mPasswordEditText = view.findViewById(R.id.password)
        mCustomerKeyboard = view.findViewById(R.id.keyboard)
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this)
        mPasswordEditText.setOnPasswordFullListener(this)
        mPasswordEditText.isEnabled = false
        return view
    }

    override fun click(number: String) {
        mPasswordEditText.addPassword(number)
    }

    override fun delete() {
        mPasswordEditText.deleteLastPassword()
    }

    override fun PasswordFull() {
        Toast.makeText(context, "密码填充完毕：" + mPasswordEditText.text, Toast.LENGTH_SHORT).show()
    }
}
