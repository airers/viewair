package com.chaijiaxun.pm25tracker;


import android.content.res.Configuration;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import android.app.Application;
import com.orm.SugarApp;
import com.orm.SugarContext;

public class SugarApplication extends SugarApp {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
        SensorReading.findById(SensorReading.class, (long) 1);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}