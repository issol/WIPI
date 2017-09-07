package com.example.isolatorv.wipi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        TMapView tMapView = new TMapView(this);

        tMapView.setSKPMapApiKey("9b47693d-6b37-3bc5-b943-2bdd1340c5ee");

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setSightVisible(true);
        relativeLayout.addView(tMapView);
        setContentView(relativeLayout);
    }
}
