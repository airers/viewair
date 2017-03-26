package com.chaijiaxun.pm25tracker;

/**
 * Lists the paired bluetooth devices
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.chaijiaxun.pm25tracker.bluetooth.BTConnectCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BTPacketCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BluetoothConnector;
import com.chaijiaxun.pm25tracker.bluetooth.BluetoothService;
import com.chaijiaxun.pm25tracker.utils.AppData;

import java.util.Set;

public class BTDeviceFragment extends Fragment {

    private static final String TAG = "BTDeviceFragment";

    // TODO: Move into some bluetooth manager
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnector mBluetoothConnector;
    private BluetoothService mBluetoothService;

    private BluetoothDevice hardcodedDevice;
    private Button scanButton;

    private ListView pairedListView, availableListView;

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

        getPairedDevices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bt_devices, container, false);
        scanButton = (Button) view.findViewById(R.id.button_scan);
        pairedListView = (ListView) view.findViewById(R.id.listview_paired);
        availableListView = (ListView) view.findViewById(R.id.listview_available);

        final BTPacketCallback packetCallback = new BTPacketCallback() {
            @Override
            public void packetReceived(BluetoothSocket socket, byte[] data, int bytesReceived) {
                if ( data != null ) {
                    Log.d(TAG, new String(data));
                }
            }
        };
        final BTConnectCallback connectCallback = new BTConnectCallback() {
            @Override
            public void deviceConnected(BluetoothSocket s) {
                Log.d(TAG, "Device " + s.getRemoteDevice().getName() + " connected");
                mBluetoothService = new BluetoothService(s);
                mBluetoothService.setCallback(packetCallback);
            }
        };

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mBluetoothService != null ) {
                    byte [] data = "Hello".getBytes();
                    mBluetoothService.write(data);
                    Log.d(TAG, "Writing stuff");
                } else if ( hardcodedDevice != null ) {
                    Log.d(TAG, "Starting the bluetooth connector");
                    mBluetoothConnector = new BluetoothConnector(hardcodedDevice, connectCallback);
                    mBluetoothConnector.start();
                } else {
                    Log.d(TAG, "No device to connect to");
                }

            }
        });
        return view;

    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "There are " + pairedDevices.size() + " devices paired");
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.d(TAG, deviceName + " " + deviceHardwareAddress);

                if ( deviceName.equals("HC-05") ) {
                    hardcodedDevice = device;
                    break;
                }
            }
        }
    }




}
