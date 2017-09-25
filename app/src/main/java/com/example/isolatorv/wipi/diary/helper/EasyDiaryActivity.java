package com.example.isolatorv.wipi.diary.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;



/**
 * Created by hanjoong on 2017-05-03.
 */

public class EasyDiaryActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        boolean enableLock = CommonUtils.loadBooleanPreference(EasyDiaryActivity.this, "application_lock");
        if (enableLock) {
            long currentMillis = System.currentTimeMillis();
            CommonUtils.saveLongPreference(EasyDiaryActivity.this, DiaryConstants.PAUSE_MILLIS, currentMillis);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean enableLock = CommonUtils.loadBooleanPreference(EasyDiaryActivity.this, "application_lock");
        long pauseMillis = CommonUtils.loadLongPreference(EasyDiaryActivity.this, DiaryConstants.PAUSE_MILLIS, 0);

    }
}
