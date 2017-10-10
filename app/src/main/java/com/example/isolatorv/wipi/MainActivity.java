package com.example.isolatorv.wipi;



import android.os.Bundle;

import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.widget.Toast;


import com.example.isolatorv.wipi.adapter.BottomBarHolderActivity;
import com.example.isolatorv.wipi.adapter.NavigationPage;
import com.example.isolatorv.wipi.fragment.DiaryFragment;
import com.example.isolatorv.wipi.fragment.FeedFragment;
import com.example.isolatorv.wipi.fragment.MapFragment;
import com.example.isolatorv.wipi.fragment.OptionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isolatorv on 2017. 9. 21..
 */



public class MainActivity extends BottomBarHolderActivity implements FeedFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener{

    private static final String TAG = "main_example";

    private Object obj1,obj2;

    List<NavigationPage> navigationPages = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp), FeedFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Support", ContextCompat.getDrawable(this, R.drawable.ic_mail_black_24dp), MapFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Billing", ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), DiaryFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp), OptionFragment.newInstance());


        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setUpBottomBarHolderActivity(navigationPages);
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
