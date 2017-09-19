package com.example.isolatorv.wipi.diary.diaries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.FontUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hanjoong on 2017-05-03.
 */

public class LockDiaryActivity extends Activity {

    @BindView(R.id.pass1)
    TextView mPass1;

    @BindView(R.id.pass2)
    TextView mPass2;

    @BindView(R.id.pass3)
    TextView mPass3;

    @BindView(R.id.pass4)
    TextView mPass4;

    @BindView(R.id.infoMessage)
    TextView mInfoMessage;

    TextView[] mPasswordView = new TextView[4];

    private int mCursorIndex = 0;

    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_diary);
        ButterKnife.bind(this);
        FontUtils.setTypeface(this, getAssets(), mInfoMessage);

        mPasswordView[0] = mPass1;
        mPasswordView[1] = mPass2;
        mPasswordView[2] = mPass3;
        mPasswordView[3] = mPass4;
    }

    @OnClick({R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9})
    void onClick(View view) {

        switch(view.getId()) {
            case R.id.num0:
                password += "0";
                break;
            case R.id.num1:
                password += "1";
                break;
            case R.id.num2:
                password += "2";
                break;
            case R.id.num3:
                password += "3";
                break;
            case R.id.num4:
                password += "4";
                break;
            case R.id.num5:
                password += "5";
                break;
            case R.id.num6:
                password += "6";
                break;
            case R.id.num7:
                password += "7";
                break;
            case R.id.num8:
                password += "8";
                break;
            case R.id.num9:
                password += "9";
                break;
        }
        mPasswordView[mCursorIndex].setText("*");

        if (mCursorIndex == 3) {

            // 검증프로세스
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (StringUtils.equals(CommonUtils.loadStringPreference(LockDiaryActivity.this, "application_lock_password", "0000"), password)) {
                                long currentMillis = System.currentTimeMillis();
                                CommonUtils.saveLongPreference(LockDiaryActivity.this, DiaryConstants.PAUSE_MILLIS, currentMillis);
                                finish();
                            } else {
                                mCursorIndex = 0;
                                password = "";
                                for (TextView tv : mPasswordView) {
                                    tv.setText(null);
                                }
                            }
                        }
                    });
                }
            }).start();

        } else {
            mCursorIndex++;

        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent readDiaryIntent = new Intent(LockDiaryActivity.this, ReadDiaryActivity.class);
        readDiaryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        readDiaryIntent.putExtra("app_finish", true);
        startActivity(readDiaryIntent);
        finish();
    }
}
