package com.fenger.fragmentdemo.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fenger.fragmentdemo.customview.LetterSideBar;
import com.fenger.fragmentdemo.R;

/**
 * com.fenger.fragmentdemo.Fragments
 * Created by fenger
 * in 2020-01-06
 */
public class FindFragment extends Fragment implements LetterSideBar.TouchLetterListener {

    private TextView textView;
    private LetterSideBar letterSideBar;
    private final int MSG = 0x0011;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setVisibility(View.GONE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        textView = view.findViewById(R.id.text);
        letterSideBar = view.findViewById(R.id.letter_side_bar);
        letterSideBar.setMyOnTouchListener(this);

        return view;
    }

    @Override
    public void touch(CharSequence letter, boolean isTouch) {
        textView.setText(letter);
        if (isTouch) {
            textView.setVisibility(View.VISIBLE);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG, 1000);
        }
    }
}
