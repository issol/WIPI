package com.example.isolatorv.wipi.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.isolatorv.wipi.MainActivity;
import com.example.isolatorv.wipi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isolatorv on 2017. 9. 27..
 */

public class BottomBarHolderActivity extends AppCompatActivity implements BottomNavigationBar.BottomNavigationMenuClickListener {

    private BottomNavigationBar mBottomNav;

    private List<NavigationPage> mNavigationPageList = new ArrayList<>();
    private String userName;
    private String userEmail;
    private String petName;
    private String petType;
    private String petAge;
    private int userSno;
    private String unique_id;
    private MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_bar_holder);

    }

    public void setUpBottomBarHolderActivity(List<NavigationPage> pages, int sno, String name, String email,String unique_id){
        userSno = sno;
        userName = name;
        userEmail = email;
        this.unique_id = unique_id;

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
                Bundle bundle = new Bundle();
                bundle.putInt("sno", userSno);
                bundle.putString("name", userName);
                bundle.putString("email", userEmail);
                bundle.putString("unique_id", unique_id);
                fragment.setArguments(bundle);
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }
    }
}

