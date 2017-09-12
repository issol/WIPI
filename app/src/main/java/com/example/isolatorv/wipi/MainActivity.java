package com.example.isolatorv.wipi;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends FragmentActivity {

    private static long back_pressed;
    BottomBar bottomBar;

    DogRiceFragment dogRiceFragment;
    DiaryFragment diaryFragment;
    MapFragment mapFragment;
    OptionFragment optionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Justice();

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_dog_rice:
                        openFragment(dogRiceFragment);
                        break;
                    case R.id.tab_map:
                        openFragment(mapFragment);
                        break;
                    case R.id.tab_diary:
                        openFragment(diaryFragment);
                        break;
                    case R.id.tab_option:
                        openFragment(optionFragment);
                        break;
                }
            }
        });

    }


    //변수,객체 선언 및 정의 매서드
    /*openFragment**********************************************************************************/
    private void Justice(){
        //하단 바 정의
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);

        //플래그먼트 정의
        dogRiceFragment = new DogRiceFragment();
        diaryFragment = new DiaryFragment();
        mapFragment = new MapFragment();
        optionFragment = new OptionFragment();
    }
    /*openFragment**********************************************************************************/

    //플래그먼트 오픈 매서드
    /*openFragment**********************************************************************************/
    private void openFragment(Fragment fragment)   {
        //플래그먼트 매니져 android.support.app.v4.fragment 연결방법
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.contentContainer,fragment);
        transaction.addToBackStack(null);   //스택에 저장 -뒤로가기 버튼구별
        transaction.commit();
    }
    /*openFragment**********************************************************************************/

    //뒤로가기 버튼 눌렀을때 호출
    /*onBackPressed********************************************************************************/
    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition

            if (back_pressed + 2000 > System.currentTimeMillis()){
                super.onBackPressed();
                finish();
            } else{
                Toast.makeText(getBaseContext(), "한번 더 뒤로가기를 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }

    }
    /*onBackPressed********************************************************************************/
}
