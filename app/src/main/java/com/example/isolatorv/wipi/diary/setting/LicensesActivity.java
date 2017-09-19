package com.example.isolatorv.wipi.diary.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.helper.EasyDiaryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CHO HANJOONG on 2017-02-11.
 */

public class LicensesActivity extends EasyDiaryActivity {

    @BindView(R.id.licenses) WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        ButterKnife.bind(this);
        webView.loadUrl("file:///android_asset/licenses.html");
    }

}
