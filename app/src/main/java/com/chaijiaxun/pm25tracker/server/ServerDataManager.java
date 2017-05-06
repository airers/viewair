package com.chaijiaxun.pm25tracker.server;

import com.chaijiaxun.pm25tracker.database.DatabaseDevice;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

/**
 * Handles all the server data syncing
 */
public class ServerDataManager {

    private static final String BASE_URL = "http://aircyclopedia.justyeo.com/api/v1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getLatestTime(AsyncHttpResponseHandler handler) {
        client.get(BASE_URL + "latest_reading", handler);
    }

    /**
     * Sets the server ID of a
     * @param device
     */
    public static void setServerID(DatabaseDevice device) {
        if ( device.getServerID() == null ) {

            client.get(BASE_URL + "register_device", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }
    }
}
