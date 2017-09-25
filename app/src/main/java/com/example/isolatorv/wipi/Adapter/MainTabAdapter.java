package com.example.isolatorv.wipi.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.isolatorv.wipi.DiaryFragment;
import com.example.isolatorv.wipi.FeedFragment;
import com.example.isolatorv.wipi.MapFragment;
import com.example.isolatorv.wipi.Option2Fragment;
import com.example.isolatorv.wipi.Option3Fragment;
import com.example.isolatorv.wipi.OptionFragment;
import com.example.isolatorv.wipi.TestActivity;

import com.example.isolatorv.wipi.login.ProfileFragment;


/**
 * 适配器
 * Created by qiudengjiao on 2017/5/6.
 */

public class MainTabAdapter extends FragmentPagerAdapter {

    private TestActivity mContext;

    private FeedFragment feedFragment;
    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;
    private OptionFragment optionFragment;
    private Option2Fragment option2Fragment;
    private Option3Fragment option3Fragment;



    public MainTabAdapter(TestActivity mainActivity) {
        super(mainActivity.getSupportFragmentManager());
        this.mContext = mainActivity;

        //初始化Fragment
        feedFragment = new FeedFragment();
        mapFragment = new MapFragment();
        diaryFragment = new DiaryFragment();
        optionFragment = new OptionFragment();
        option2Fragment = new Option2Fragment();
        option3Fragment = new Option3Fragment();


    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return feedFragment;
        } else if (position == 1) {
            return mapFragment;
        } else if (position == 2) {
            return diaryFragment;
        } else if (position == 3) {
            return optionFragment;
        } else if (position == 4){
            return option3Fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
