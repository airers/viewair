package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothSocket;

/**
 * The function that gets called when a client is connected to the server
 */
public interface BTConnectCallback {
    void deviceConnected(BluetoothSocket s);
    void unableToConnect(final String message);
}