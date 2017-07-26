package com.chaijiaxun.pm25tracker.utils;

import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Store and retrieve the device information
 */

public class HandsetInfo {
    HandsetInfo() {

    }

    private static String handsetID; // Try to get advertising ID, if not Android ID
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
            handsetID = adInfo.getId();
        } else {
            handsetID = Settings.Secure.getString(AppData.getInstance().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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

    public static String getHandsetID() {
        return handsetID;
    }

    public static String getInfoString() {
        return
        "Android ID: " + getHandsetID() +  "\n" +
        "SDK: " + androidSDKVersion + "\n" +
        "Release: " + androidVersionRelease + "\n" +
        "Manufacturer: " + manufacturer + "\n" +
        "Market Name: " + name + "\n" +
        "Model Name: " + model + "\n" +
        "Codename: " + codename + "\n" +
        "Device Name: " + deviceName + "\n";
    }

    public static JSONObject getInfoJSON() {
        JSONObject handsetInfo = new JSONObject();
        try {
            handsetInfo.put("manufacturer", manufacturer);
            handsetInfo.put("marketName", name);
            handsetInfo.put("modelName", model);
            handsetInfo.put("codename", codename);
            handsetInfo.put("deviceName", deviceName);
            handsetInfo.put("androidVersionRelease", androidVersionRelease);
            handsetInfo.put("androidSDKVersion", androidSDKVersion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return handsetInfo;
    }
}
