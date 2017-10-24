package com.example.isolatorv.wipi.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

/**
 * Created by isolatorv on 2017. 9. 27..
 */

public class NavigationPage {

    private String title;
    private Drawable icon;
    private Fragment fragment;

    public NavigationPage(String title, Drawable icon, Fragment fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
