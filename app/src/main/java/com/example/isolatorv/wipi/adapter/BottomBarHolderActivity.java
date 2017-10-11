package com.example.isolatorv.wipi.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.isolatorv.wipi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isolatorv on 2017. 9. 27..
 */

public class BottomBarHolderActivity extends AppCompatActivity implements BottomNavigationBar.BottomNavigationMenuClickListener {

    private BottomNavigationBar mBottomNav;

    private List<NavigationPage> mNavigationPageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar_holder);
    }

    public void setUpBottomBarHolderActivity(List<NavigationPage> pages){
        if (pages.size() != 4) {
            throw new RuntimeException("List of NavigationPage must contain 4 members.");
        } else {
            mNavigationPageList = pages;
            mBottomNav = new BottomNavigationBar(this, pages, this);
            setupFragments();
        }
    }

    private void setupFragments(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, mNavigationPageList.get(0).getFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClickedOnBottomNavigationMenu(int menuType) {
        Fragment fragment = null;
        switch (menuType) {
            case BottomNavigationBar.MENU_BAR_1:
                fragment = mNavigationPageList.get(0).getFragment();
                break;
            case BottomNavigationBar.MENU_BAR_2:
                fragment = mNavigationPageList.get(1).getFragment();
                break;
            case BottomNavigationBar.MENU_BAR_3:
                fragment = mNavigationPageList.get(2).getFragment();
                break;
            case BottomNavigationBar.MENU_BAR_4:
                fragment = mNavigationPageList.get(3).getFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }
    }
}

