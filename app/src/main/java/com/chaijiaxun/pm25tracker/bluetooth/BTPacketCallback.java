package com.chaijiaxun.pm25tracker.bluetooth;


import android.bluetooth.BluetoothSocket;

/**
 * Simple callback method to wrap the handlers.
 * Gets called on bluetooth packet data receipt
 */

public interface BTPacketCallback {
    void packetReceived(BluetoothSocket socket, byte[] data, int bytesReceived);
}
