package com.chaijiaxun.pm25tracker.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Singleton class that stores all the global variables in the app
 */

public class AppData {
    static private final String PREFS_NAME = "ViewairPrefs";
    static private final String LAST_DEVICE = "LastDevice";
    static private final String LAST_SERVER_SYNC = "LastServerSync";
    static private final String EULA_ACCEPTED = "EULAAccepted";
    static private final String TIMEZONE_OFFSET = "TimezoneOffset";

    private BluetoothAdapter bluetoothAdapter;
    private int packetsLeft;
    private int totalPackets;

    private TextView messageText;
    private ProgressBar transferProgress;

    private static AppData instance = new AppData();
    public static AppData getInstance() {
        return instance;
    }


    private AppData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private Context appContext;

    public Calendar getLastServerSync() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        long lastSyncTimestamp = settings.getLong(LAST_SERVER_SYNC, 0);
        if ( lastSyncTimestamp == 0 ) {
            return null;
        }
        return DataUtils.millsToDate(lastSyncTimestamp);
    }

    public void setLastServerSync() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(LAST_SERVER_SYNC, Calendar.getInstance().getTimeInMillis());

        // Commit the edits!
        editor.apply();
    }

    public boolean acceptedEULA() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(EULA_ACCEPTED, false);
    }

    public void acceptEULA() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(EULA_ACCEPTED, true);

        // Commit the edits!
        editor.apply();
    }

    public int getTimezoneOffset() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int timezoneOffset = 0;
        if (!settings.contains(TIMEZONE_OFFSET)) {
            timezoneOffset = TimezoneUtils.getPhoneTimezone();
            setTimezoneOffset(timezoneOffset);
        } else {
            timezoneOffset = settings.getInt(TIMEZONE_OFFSET, 0);
        }

        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezone) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(TIMEZONE_OFFSET, timezone);

        // Commit the edits!
        editor.apply();
    }

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
        initSettings();
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

    private void initSettings() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        if ( !settings.contains(SettingType.AUTOCONNECT.toString()) ) {
            saveSetting(SettingType.AUTOCONNECT, true);
            saveSetting(SettingType.AUTOCONNECT_DEVICE, true);
            saveSetting(SettingType.AUTOSYNC, true);
            saveSetting(SettingType.STATUSBAR, true);
            saveSetting(SettingType.CLOUD, true);
        }

    }
    public boolean getSetting(SettingType type) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(type.toString(), false);
    }
    public void saveSetting(SettingType type, boolean state) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(type.toString(), state);
        // Commit the edits!
        editor.apply();
    }

// Should store database stuff
    // Should store shared pref stuff
    // Should store bluetooth manager
    // Should store google maps thing
}
