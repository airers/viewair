package com.chaijiaxun.pm25tracker;

/**
 * Lists the paired bluetooth devices
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaijiaxun.pm25tracker.utils.AppData;

import java.util.Set;

public class BTDeviceFragment extends Fragment {

    private static final String TAG = "BTDeviceFragment";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnector mBluetoothConnector;

    public BTDeviceFragment() {
        mBluetoothAdapter = AppData.getInstance().getBluetoothAdapter();
    }

    public static BTDeviceFragment newInstance() {
        BTDeviceFragment fragment = new BTDeviceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bt_devices, container, false);

    }

    private void getBluetoothDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "There are " + pairedDevices.size() + " devices paired");
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.d(TAG, deviceName + " " + deviceHardwareAddress);

                if ( deviceName.equals("HC-05") ) {
                    mBluetoothConnector = new BluetoothConnector(device);
                    mBluetoothConnector.run();
                    break;
                }
            }
        }
    }




}
