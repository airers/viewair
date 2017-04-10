package com.chaijiaxun.pm25tracker.server;

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
}
