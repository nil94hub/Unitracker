package com.hizvas.doubletrack;


import androidx.multidex.MultiDexApplication;

import com.mapbox.mapboxsdk.Mapbox;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
       // Fabric.with(this, new Crashlytics());
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapboxkey));

        SessionManager.initialize(getApplicationContext());

    }


}
