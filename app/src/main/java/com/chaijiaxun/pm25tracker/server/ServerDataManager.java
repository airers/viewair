package com.chaijiaxun.pm25tracker.server;

import android.hardware.Sensor;
import android.util.Log;

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

    public static void getLatestTime(AsyncHttpResponseHandler handler) {
        client.get(BASE_URL + "latest_reading", handler);
    }


    /**
     * Sends the readings from a certain point
     */
    public static void sendDeviceReadings(DatabaseDevice device) {
        device = DatabaseDevice.getList().get(0);
        long time = 0;
        List<SensorReading> readings = Select.from(SensorReading.class).
                where(
                        Condition.prop(NamingHelper.toSQLNameDefault("localDeviceID")).eq(device.getId()),
                        Condition.prop(NamingHelper.toSQLNameDefault("time")).gt(time)
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

        String requestPayload = readingsJSON.toString();
        Log.d(TAG, requestPayload);

    }

    /**
     * Gets the server ID from the server
     */
    public static void setServerID(DatabaseDevice device) {
//        if ( device.getServerID() == null ) {
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
                                    if (responseJSON.has("id") && responseJSON.has("lastReading")) {
                                        Log.d(TAG, "Valid JSON");
                                        Log.d(TAG, "ID: " + responseJSON.getString("id"));
                                        Log.d(TAG, "LastReading: " + responseJSON.getString("lastReading"));
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
//        }
    }
}
