package com.example.isolatorv.wipi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.isolatorv.wipi.login.JoinActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler() , 3000);
        
    }

    private class splashHandler implements Runnable{
        public void run(){
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
