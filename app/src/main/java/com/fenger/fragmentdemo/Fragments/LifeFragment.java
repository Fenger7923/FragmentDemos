package com.fenger.fragmentdemo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fenger.fragmentdemo.customview.CustomerKeyboard;
import com.fenger.fragmentdemo.customview.PasswordEditText;
import com.fenger.fragmentdemo.R;

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
public class LifeFragment extends Fragment implements CustomerKeyboard.CustomerKeyboardClickListener, PasswordEditText.PasswordFullListener {

    private CustomerKeyboard mCustomerKeyboard;
    private PasswordEditText mPasswordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        mPasswordEditText = view.findViewById(R.id.password);
        mCustomerKeyboard = view.findViewById(R.id.keyboard);

        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this);
        mPasswordEditText.setOnPasswordFullListener(this);
        mPasswordEditText.setEnabled(false);

        return view;
    }

    @Override
    public void click(String num) {
        mPasswordEditText.addPassword(num);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
    }

    @Override
    public void PasswordFull() {
        Toast.makeText(getContext(), "密码填充完毕：" + mPasswordEditText.getText(), Toast.LENGTH_SHORT).show();
    }
}
