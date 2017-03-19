package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnector extends Thread {
    private static final String TAG = "APPBluetoothClient";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    private BTConnectCallback callback;

    public BluetoothConnector(BluetoothDevice device, BTConnectCallback callback) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        ParcelUuid [] uuids = mmDevice.getUuids();

        Log.d(TAG, "UUID Count " + uuids.length);

        this.callback = callback;



        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
//        mBluetoothAdapter.cancelDiscovery();
        Log.d(TAG, "Connecting");
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            Log.d(TAG, "Unable to connect to socket");
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        Log.d(TAG, "Connected to bluetooth socket");
        if ( callback != null ) {
            callback.deviceConnected(mmSocket);
        }

//        try {
//            InputStream stream = mmSocket.getInputStream();
//            OutputStream outputStream = mmSocket.getOutputStream();
//            while ( stream.available() > 0 ) {
//                int read = stream.read();
//                Log.d(TAG, "Read: " + read);
//            }
//        } catch ( Exception e ) {
//            e.printStackTrace();
//        }


        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
//        manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
