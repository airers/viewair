package com.chaijiaxun.pm25tracker.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.SharedPreferences;

/**
 * Singleton class that stores all the global variables in the app
 */

public class AppData {
    public static final String PREFS_NAME = "ViewairPrefs";
    public static final String LAST_DEVICE = "LastDevice";

    private BluetoothAdapter bluetoothAdapter;
    private int packetsLeft;
    private int totalPackets;

    TextView messageText;
    ProgressBar transferProgress;

    private static AppData instance = new AppData();
    public static AppData getInstance() {
        return instance;
    }


    private AppData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private Context appContext;

    public String getLastDeviceUUID() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(LAST_DEVICE, null);
    }
    public void setLastDeviceUUID(String uuid) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_DEVICE, uuid);

        // Commit the edits!
        editor.apply();
    }
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
        setTransferProgress();

        if ( packetsLeft <= 0 ) {
            totalPackets = 0;
            packetsLeft = 0;
            setMessageText("Connected");
            hideTransferProgress();
        }
    }
    public double getTransferPercentage() {
        double percent =  ((double)(totalPackets - packetsLeft) / (double)totalPackets);
        Log.d("DeviceManager", "Percent: " + percent);
        return percent;
    }

    public void setPacketsLeft(int packetsLeft) {
        this.packetsLeft = this.totalPackets = packetsLeft;
        setTransferProgress();
        setMessageText("Transferring");
    }

    public void setActivityBottombar(TextView textView, ProgressBar progressBar) {
        messageText = textView;
        transferProgress = progressBar;
    }

    public void setMessageText(String text) {
        messageText.setText(text);
    }

    public void setTransferProgress() {
        transferProgress.setVisibility(View.VISIBLE);
        transferProgress.setProgress((int)(getTransferPercentage() * 100));
    }

    public void hideTransferProgress() {
        transferProgress.setVisibility(View.GONE);
    }

// Should store database stuff
    // Should store shared pref stuff
    // Should store bluetooth manager
    // Should store google maps thing
}
