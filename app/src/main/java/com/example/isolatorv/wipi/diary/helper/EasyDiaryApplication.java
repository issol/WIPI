package com.example.isolatorv.wipi.diary.helper;

import android.app.Application;

import io.realm.Realm;

public class EasyDiaryApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
