package com.example.isolatorv.wipi.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.isolatorv.wipi.MainActivity;
import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.diaries.ReadDiaryActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CHO HANJOONG on 2016-12-31.
 */

public class IntroActivity extends Activity implements Handler.Callback {

    private final int START_MAIN_ACTIVITY = 0;

    @BindView(R.id.appName)
    TextView mAppName;

    @BindView(R.id.companyName)
    TextView mCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        // determine device language
        if (!Locale.getDefault().getLanguage().matches(DiaryConstants.CUSTOM_FONTS_SUPPORT_LANGUAGE)) {
            CommonUtils.saveStringPreference(this, "font_setting", "Default");
        }

        FontUtils.setTypeface(this, getAssets(), mAppName);
        FontUtils.setTypeface(this, getAssets(), mCompanyName);
        float fontSize = CommonUtils.loadFloatPreference(this, "font_size", 0);
        if (fontSize > 0) {
            mAppName.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            mCompanyName.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }
        new Handler(this).sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 500);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case START_MAIN_ACTIVITY:
                Log.i("gg", "success");
                startActivity(new Intent(this, ReadDiaryActivity.class));
                finish();
                break;
            default:
                break;
        }
        return false;
    }

}
