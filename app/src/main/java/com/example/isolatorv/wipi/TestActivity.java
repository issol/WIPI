package com.example.isolatorv.wipi;



import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.isolatorv.wipi.Adapter.BottomBarHolderActivity;
import com.example.isolatorv.wipi.Adapter.MainNavigateTabBar;
import com.example.isolatorv.wipi.Adapter.NavigationPage;

import java.util.ArrayList;
import java.util.List;


import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by isolatorv on 2017. 9. 21..
 */



public class TestActivity extends BottomBarHolderActivity implements OptionFragment.OnMyListener, FeedFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener{

   // static ViewPager viewPager;


    private static final String TAG = "main_example";

    private Object obj1,obj2,obj3,obj4;

    private FeedFragment feedFragment;
    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;
    private OptionFragment optionFragment;
    private Option3Fragment option3Fragment;
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

        super.setupBottomBarHolderActivity(navigationPages);
    }

    @Override
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onReceivedData(Object data) {
        switch (data.toString().trim()) {
            case "hospitalOn":
            case "hospitalOFF":
                obj1 = data;
                Log.d(TAG, "main: receive " + obj1.toString().trim());
                break;
            case "shopOn":
            case "shopOFF":
                obj2 = data;
                Log.d(TAG, "main: receive " + obj2.toString().trim());
                break;
            default:
                break;
        }
    }
    /*onReceivedData********************************************************************************/

    //엑티비티에서 플래그 먼트로 데이터를 보내는 매서드들
    /*getData***************************************************************************************/
    public Object getData1(){
        if(obj1==null){
            obj1="nodata";
            Log.d(TAG,"main: send"+obj1.toString().trim());
            return obj1;
        }else
        {
            Log.d(TAG,"main: send"+obj1.toString().trim());
            return obj1;
        }

    }
    public Object getData2(){
        if(obj2==null){
            obj2="nodata";
            Log.d(TAG,"main: send"+obj2.toString().trim());
            return obj2;
        }else{
            Log.d(TAG,"main: send"+obj2.toString().trim());
            return obj2;
        }
    }

}
