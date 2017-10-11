package com.example.isolatorv.wipi.diary.helper;

import android.support.v7.app.AppCompatActivity;

import com.example.isolatorv.wipi.diary.Utils.CommonUtils;

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
