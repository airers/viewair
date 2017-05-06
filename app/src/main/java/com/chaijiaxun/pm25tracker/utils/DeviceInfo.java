package com.chaijiaxun.pm25tracker.utils;

import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.jaredrummler.android.device.DeviceName;

import java.io.IOException;

/**
 * Simple class to store the device informations
 */

public class DeviceInfo {
    DeviceInfo() {

    }

    private static String deviceID; // Try to get advertising ID, if not Android ID
    private static String manufacturer;
    private static String name;
    private static String model;
    private static String codename;
    private static String deviceName;
    private static String androidVersionRelease;
    private static int androidSDKVersion;

    public static void init() {
        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(AppData.getInstance().getApplicationContext());

        } catch (Exception e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).

        }

        if ( adInfo != null ) {
            deviceID = adInfo.getId();
        } else {
            deviceID = Settings.Secure.getString(AppData.getInstance().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        androidSDKVersion = android.os.Build.VERSION.SDK_INT;
        androidVersionRelease = android.os.Build.VERSION.RELEASE;

        DeviceName.with(AppData.getInstance().getApplicationContext()).request(new DeviceName.Callback() {
            @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                manufacturer = info.manufacturer;  // "Samsung"
                name = info.marketName;            // "Galaxy S7 Edge"
                model = info.model;                // "SAMSUNG-SM-G935A"
                codename = info.codename;          // "hero2lte"
                deviceName = info.getName();       // "Galaxy S7 Edge"
            }
        });
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static String getInfoJSON() {
        return "{" +
                "\"manufacturer\": \"" + manufacturer + "\"," +
                "\"name\": \"" + name + "\"," +
                "\"model\": \"" + model + "\"," +
                "\"codename\": \"" + codename + "\"," +
                "\"deviceName\": \"" + deviceName + "\"," +
                "\"androidVersionRelease\": \"" + androidVersionRelease + "\"," +
                "\"androidSDKVersion\": \"" + androidSDKVersion + "\"" +
                "}";
    }
}
