package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.ByteUtils;

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

    int pendingReadings = 0;

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
                if ( !isDeviceConnected() ) {
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
                    handler.postDelayed(this, 5000);
                }
            }
        };
    }

    public void processPacket(ArrayList<Byte> data) {
        Log.d(TAG, "Process packet");
        byte type = data.get(0);
        byte length = data.get(1);
        switch ( type ) {
            case BTPacket.TYPE_CONNECTION_ACK:
//                Log.d(TAG, "Connection still active");
//                Log.d(TAG, "Length: " + length);
//
//                Log.d(TAG, "Actual Length: " + data.size());
                receivedAck = true;
                if ( data.size() > 6 ) {
                    byte [] longData = {
                            data.get(2),
                            data.get(3),
                            data.get(4),
                            data.get(5)
                    };
                    long deviceTimestamp = ByteUtils.arduinoLongTSToAndroidLongTS(longData);
                    currentDevice.setDeviceTime(deviceTimestamp);
//                    Log.d(TAG, "Timestamp " + deviceTimestamp);
                }
                break;
            case BTPacket.TYPE_READING_COUNT:
                Log.d(TAG, ByteUtils.byteArrayToString(data));
                Log.d(TAG, "" + data.size());
                if ( data.size() > 4 ) {
                    byte [] countBytes = { data.get(2), data.get(3) };
                    pendingReadings = ByteUtils.arduinoUint16ToAndroidInt(countBytes);
                    Log.d(TAG, "Reading count received: " + pendingReadings);
                }
                break;
            case BTPacket.TYPE_MICROCLIMATE_PACKET:
                int microclimate = (int)data.get(2);
                currentDevice.setMicroclimate(microclimate);
                break;
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
        return currentDevice != null && bluetoothService != null;
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


    public void setMicroclimate(int microclimate) {
        if ( bluetoothService == null || currentDevice == null ) {
            return;
        }
        byte mc = (byte)microclimate;
        byte [] bytes = {BTPacket.TYPE_SET_MICROCLIMATE, mc};
        this.bluetoothService.write(bytes);
    }

    public int getPendingReadings() {
        return pendingReadings;
    }
}
