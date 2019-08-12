package com.zhn.myalbum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zhn.myalbum.ui.fragment.MyFragment;
import com.zhn.myalbum.ui.WelcomeActivity;

/**
 *自定义FragmentStatePagerAdapter 适配器
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new MyFragment(position);
    }

    @Override
    public int getCount() {
        return WelcomeActivity.getResource().length;
    }
}
