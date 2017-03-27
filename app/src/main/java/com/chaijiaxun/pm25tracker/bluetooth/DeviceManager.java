package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * Manages the device connection to the bluetooth device
 */

public class DeviceManager {
    private static final String TAG = "DeviceManager";
    static private DeviceManager singleton = new DeviceManager();
    static public DeviceManager getInstance() {
        return singleton;
    }
    private Device currentDevice;
    private BTPacketCallback packetCallback;
    private BTDisconnectCallback disconnectCallback;
    private BluetoothService bluetoothService;
    boolean receivedAck;
    int connectionStatus = 0;

    final Handler handler = new Handler();
    Runnable aliveCheck;

    ArrayList<Byte> dataQueue;

    private DeviceManager() {
        dataQueue = new ArrayList<>();
        packetCallback = new BTPacketCallback() {
            @Override
            public void packetReceived(BluetoothSocket socket, byte[] data, int bytesReceived) {
                if ( data != null ) {
                    for (byte b: data) {
                        dataQueue.add(b);
                    }
                    byte[] CRLF = "\r\n".getBytes();
                    if ( dataQueue.size() > 2 && dataQueue.get(dataQueue.size() - 2) == CRLF[0] && dataQueue.get(dataQueue.size() - 1) == CRLF[1]) {
                        processPacket(dataQueue);
                        dataQueue.clear();
                    }
                }
            }
        };

        aliveCheck = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Alive Check");
                if ( currentDevice == null || bluetoothService == null ) {
                    return;
                }
                if ( !receivedAck ) {
                    Log.d(TAG, "Have not heard from device");
                    connectionStatus++;
                    if ( connectionStatus > 3 ) {
                        unsetCurrentDevice();
                        if (disconnectCallback != null) {
                            disconnectCallback.deviceDisonnected();
                        }
                        return;
                    }
                }
                try {
                    //do your code here
                    receivedAck = false;
                    byte [] bytes = {BTPacket.TYPE_CONNECTION_CHECK};
                    bluetoothService.write(bytes);
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally {
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 2000);
                }
            }
        };
    }

    public void processPacket(ArrayList<Byte> data) {
        if ( data.get(0) == BTPacket.TYPE_CONNECTION_ACK ) {
            Log.d(TAG, "Connection still active");
            receivedAck = true;
        }
    }
    public void unsetCurrentDevice() {
        if ( bluetoothService != null ) {
            bluetoothService.destroy();
        }
        connectionStatus = 0;
        currentDevice = null;
    }
    public void setCurrentDevice(BluetoothDevice d) {
        setCurrentDevice(new Device(d));
    }

    public void setCurrentDevice(Device d) {
        currentDevice = d;
    }

    public boolean hasLastDevice() {
        return currentDevice != null;
    }

    public boolean isDeviceConnected() {
        return false;
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public BluetoothService getBluetoothService() {
        return bluetoothService;
    }

    public void setBluetoothService(BluetoothSocket socket) {
        this.bluetoothService = new BluetoothService(socket);
        this.bluetoothService.setCallback(packetCallback);
        connectionStatus = 1;
        aliveCheck.run();
    }

    public void setDisconnectCallback(BTDisconnectCallback disconnectCallback) {
        this.disconnectCallback = disconnectCallback;
    }
}
