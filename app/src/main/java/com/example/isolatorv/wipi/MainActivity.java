package com.example.isolatorv.wipi;



import android.os.Bundle;

import android.support.v4.content.ContextCompat;




import android.widget.Toast;


import com.example.isolatorv.wipi.adapter.BottomBarHolderActivity;
import com.example.isolatorv.wipi.adapter.NavigationPage;
import com.example.isolatorv.wipi.fragment.DiaryFragment;
import com.example.isolatorv.wipi.fragment.FeedFragment;
import com.example.isolatorv.wipi.fragment.MapFragment;
import com.example.isolatorv.wipi.fragment.MyProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isolatorv on 2017. 9. 21..
 */
/*
 수정 날짜 : 2017-10-02
 수정한 부분 45번부터 48번 아이콘 수정(확인하면 지워)
 */


public class MainActivity extends BottomBarHolderActivity implements FeedFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener{

    private static final String TAG = "main_example";


    private Object obj1,obj2;
    private String userName;
    private String userEmail;
    private String petName;
    private String petType;
    private String petAge;
    private int sno;
    private String unique_id;


    List<NavigationPage> navigationPages = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ProfileData profile = extras.getParcelable("userInfo");
        sno = profile.getSno();
        userName= profile.getUserName();
        userEmail = profile.getUserEmail();
        unique_id = profile.getUniqueID();
        petName = profile.getP_name();
        petType = profile.getP_type();
        petAge = profile.getP_age();


        NavigationPage page1 = new NavigationPage("Feed", getResources().getDrawable(R.drawable.wipi_feed), FeedFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Map", ContextCompat.getDrawable(this, R.drawable.wipi_map), MapFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Diary", ContextCompat.getDrawable(this, R.drawable.wipi_diary), DiaryFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.wipi_profile), MyProfileFragment.newInstance());


        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);


        super.setUpBottomBarHolderActivity(navigationPages, sno, userName, userEmail,unique_id );
    }

    @Override
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }




}
