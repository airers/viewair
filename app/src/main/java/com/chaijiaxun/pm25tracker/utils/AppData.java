package com.chaijiaxun.pm25tracker.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * Singleton class that stores all the global variables in the app
 */

public class AppData {
    private BluetoothAdapter bluetoothAdapter;
    private int packetsLeft;

    private static AppData instance = new AppData();
    public static AppData getInstance() {
        return instance;
    }


    private AppData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    private Context appContext;
    public void init(Context appContext) {
        this.appContext = appContext;
    }

    public Context getApplicationContext() {
        return appContext;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public int getPacketsLeft() {
        return packetsLeft;
    }

    public void decrementPacketsLeft() {
        packetsLeft--;
    }

    public void setPacketsLeft(int packetsLeft) {
        this.packetsLeft = packetsLeft;
    }

// Should store database stuff
    // Should store shared pref stuff
    // Should store bluetooth manager
    // Should store google maps thing
}
