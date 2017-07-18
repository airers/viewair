package com.chaijiaxun.pm25tracker.server;

import android.hardware.Sensor;
import android.util.Log;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.database.DatabaseDevice;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.DataUtils;
import com.loopj.android.http.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Handles all the server data syncing
 */
public class ServerDataManager {

    private static final String BASE_URL = "http://128.199.69.0/api/v1";
    private static final String TAG = "ServerDataManager";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * Assumes that the device already has a serverID
     */
    public static void getLatestTime(DatabaseDevice device, AsyncHttpResponseHandler handler) {
        client.get(BASE_URL + "devices/"+device.getServerID()+"/readings/latest", handler);
    }


    private static void sendReadings(long startTime, DatabaseDevice device) {

        List<SensorReading> readings = Select.from(SensorReading.class).
                where(
                        Condition.prop(NamingHelper.toSQLNameDefault("localDeviceID")).eq(device.getId()),
                        Condition.prop(NamingHelper.toSQLNameDefault("time")).gt(startTime)
                ).
                orderBy("time").
                limit("5").
                list();

        JSONArray readingsJSON = new JSONArray();
        // Convert readings into JSON
        for ( SensorReading reading : readings ) {
            try {
                JSONObject readingJSON = new JSONObject();
                Calendar cal = DataUtils.millsToDate(reading.getTime());

                readingJSON.put("time", DataUtils.dateToSQLDate(cal));
                readingJSON.put("pm25", reading.getPollutantLevel());
                readingJSON.put("microclimate", reading.getMicroclimate());
                readingJSON.put("locationLat", reading.getLocationLat());
                readingJSON.put("locationLon", reading.getLocationLon());
                readingJSON.put("locationAcc", reading.getLocationAccuracy());
                readingJSON.put("locationEle", reading.getLocationElevation());

                readingsJSON.put(readingJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            String requestPayload = readingsJSON.toString();
            Log.d(TAG, requestPayload);

            StringEntity entity = new StringEntity(requestPayload);
            client.post(AppData.getInstance().getApplicationContext(),
                    BASE_URL + "/devices/" + device.getServerID() + "/readings",
                    entity,
                    "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String response = new String(responseBody);
                            try {
                                Log.d(TAG, response);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d(TAG, "Failure: " + new String(responseBody));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Sends the readings from a certain point
     */
    public static void sendDeviceReadings(final DatabaseDevice device) {
        if ( device == null ) {
            return;
        }
        if ( device.getServerID() == null || device.getServerID().length() == 0 ) {
            Toast.makeText(AppData.getInstance().getApplicationContext(), "Can't sync, device not registered", Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(BASE_URL + "/devices/"+device.getServerID()+"/readings/latest", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    Log.d(TAG, response);

                    JSONObject responseJSON = new JSONObject(response);
                    if (responseJSON.optBoolean("exist", false) && responseJSON.has("reading")) {
                        Log.d(TAG, "Latest reading");
                        JSONObject reading = (JSONObject)responseJSON.opt("reading");
                        if ( reading != null ) {
                            String deviceTime = reading.optString("deviceTime");

                            long timestamp = DataUtils.SQLTimestampToMills(deviceTime);
                            Log.d(TAG, "Reading Time: " + deviceTime + " " + timestamp);
                            sendReadings(timestamp, device);
                        }
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    /**
     * Gets the server ID from the server
     * Overrides existing ID
     * TODO: Check if ID mismatch exists
     */
    public static void setServerID(final DatabaseDevice device) {
        if ( device != null ) {
            JSONObject phoneInfo = new JSONObject();
            JSONObject jsonParams = new JSONObject();

            try {
                phoneInfo.put("model", "Regular Phone");
                phoneInfo.put("type", "Android");
                jsonParams.put("phoneUuid", "1234567890abcdef");
                jsonParams.put("sensorUuid", "fedcba0987654321");
                jsonParams.put("phoneInfo", phoneInfo);
                StringEntity entity = new StringEntity(jsonParams.toString());

                client.post(AppData.getInstance().getApplicationContext(),
                        BASE_URL + "/devices/register",
                        entity,
                        "application/json",
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String response = new String(responseBody);
                                try {
                                    Log.d(TAG, response);

                                    JSONObject responseJSON = new JSONObject(response);
                                    String serverID = responseJSON.getString("id");
                                    if (responseJSON.has("id") && responseJSON.has("lastReading")) {
                                        Log.d(TAG, "Valid JSON");
                                        Log.d(TAG, "ID: " + serverID);
                                        Log.d(TAG, "LastReading: " + responseJSON.getString("lastReading"));
                                        if ( device != null ) {
                                            device.setServerID(serverID);
                                            device.save();
                                        }
                                    }
                                } catch ( Exception e ) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.d(TAG, "Failure: " + new String(responseBody));

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
