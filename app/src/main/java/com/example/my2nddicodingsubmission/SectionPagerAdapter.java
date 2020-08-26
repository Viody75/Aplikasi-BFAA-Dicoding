package com.example.my2nddicodingsubmission;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private String username;

    public SectionPagerAdapter(@NonNull FragmentManager fm, Context mContext, String mUsername) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
        this.username = mUsername;
    }


    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.following,
            R.string.follower
    };



    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FollowingFragment().newInstance(username);
                break;
            case 1:
                fragment = new FollowerFragment().newInstance(username);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
