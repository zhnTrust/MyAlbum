package com.zhn.myalbum.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhn.myalbum.ui.WelcomeActivity;

@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {
    private int position;

    public MyFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(WelcomeActivity.getResource()[position]);
        return imageView;
    }
}
