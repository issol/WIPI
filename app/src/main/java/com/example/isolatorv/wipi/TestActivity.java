package com.example.isolatorv.wipi;



import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isolatorv.wipi.Adapter.MainTabAdapter;

import java.util.ArrayList;


import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by isolatorv on 2017. 9. 21..
 */

public class TestActivity extends AppCompatActivity implements OptionFragment.OnMyListener{

    private int mSelectedPosition;
    private Fragment FeedFragment;
    private Fragment MapFragment;
    private Fragment DiaryFragment;
    private Fragment OptionFragment;

    private Fragment mFragment;

   // static ViewPager viewPager;
    private FragmentPagerAdapter mAdapter;

    private static final String TAG = "main_example";

    private Object obj1,obj2,obj3,obj4;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);
        initUI();
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);


        FeedFragment = new FeedFragment();
        MapFragment = new DiaryFragment();
        DiaryFragment = new MapFragment();
        OptionFragment = new OptionFragment();

        mAdapter = new MainTabAdapter(this);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);

        // myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

       /* viewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 5;
            }



            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }


            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp, null, false);

                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
                txtPage.setText(String.format("Page #%d", position));

                container.addView(view);
                return view;
            }
        });*/

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.image_feed),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Feed")
                        .badgeTitle("food")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.image_map),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Map")
                        .badgeTitle("Follow")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.image_diary),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Diary")
                        .badgeTitle("Write")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.image_setting),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Setting")
                        .badgeTitle("Fuck")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.image_mypage),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("MyPage")
                        .badgeTitle("You")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                mSelectedPosition = position;

                switch (position){
                  /*  case 0:
                        switchFragment(FeedFragment);
                        break;
                    case 1:
                        switchFragment(MapFragment);
                        break;
                    case 2:
                        switchFragment(DiaryFragment);
                        break;
                    case 3:
                        switchFragment(OptionFragment);
                        break;*/
                }

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
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
