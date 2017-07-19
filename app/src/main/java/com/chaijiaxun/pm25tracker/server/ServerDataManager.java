package com.chaijiaxun.pm25tracker.server;

import android.util.Log;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.database.DatabaseDevice;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.DataUtils;
import com.chaijiaxun.pm25tracker.utils.HandsetInfo;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
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


    private static void sendReadings(DatabaseDevice device, long startTime) {

        int readingsLeft = DataUtils.getReadingCountFrom(device, startTime);

        Log.d(TAG, "Sending readings. Readings left: " + readingsLeft);
        if ( readingsLeft == 0 ) {
            Log.d(TAG, "No more readings, aborting");
            return;
        }

        List<SensorReading> readings = DataUtils.getReadingsFrom(device, startTime, 100);

        JSONArray readingsJSON = new JSONArray();
        // Convert readings into JSON
        for ( SensorReading reading : readings ) {
            try {
                JSONObject readingJSON = new JSONObject();
                Calendar cal = DataUtils.millsToDate(reading.getTime());

                readingJSON.put("deviceTime", DataUtils.dateToSQLDate(cal));
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
                                JSONObject responseJSON = new JSONObject(response);
                                int passed = responseJSON.optInt("pass", -1);
                                int failed = responseJSON.optInt("fail", -1);

                                Log.d(TAG, "Passed: " + passed + "/" + (passed + failed));
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
                    long timestamp = 0;
                    if (responseJSON.optBoolean("exist", false) && responseJSON.has("reading")) {
                        Log.d(TAG, "Latest reading");
                        JSONObject reading = (JSONObject)responseJSON.opt("reading");
                        if ( reading != null ) {
                            String deviceTime = reading.optString("deviceTime");

                            timestamp = DataUtils.SQLTimestampToMills(deviceTime);
                            Log.d(TAG, "Reading Time: " + deviceTime + " " + timestamp);
                        }
                    } else {
                        // No existing readings
                        Log.d(TAG, "No Readings");
                    }
                    sendReadings(device, timestamp);

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
            JSONObject phoneInfo = HandsetInfo.getInfoJSON();
            JSONObject jsonParams = new JSONObject();
            String handsetID = HandsetInfo.getHandsetID();
            String sensorID = device.getUuid();

            if ( handsetID.length() == 0 || sensorID.length() == 0 ) {
                Toast.makeText(AppData.getInstance().getApplicationContext(), "Missing Device IDs, cannot sync", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                jsonParams.put("phoneUuid", handsetID);
                jsonParams.put("sensorUuid", sensorID);
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
