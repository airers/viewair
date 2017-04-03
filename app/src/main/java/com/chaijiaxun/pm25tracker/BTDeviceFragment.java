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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.bluetooth.BTConnectCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BTPacketCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BluetoothConnector;
import com.chaijiaxun.pm25tracker.bluetooth.BluetoothService;
import com.chaijiaxun.pm25tracker.bluetooth.DeviceManager;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.UIUtils;

import java.util.Set;

public class BTDeviceFragment extends Fragment {

    private static final String TAG = "BTDeviceFragment";

    // TODO: Move into some bluetooth manager
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnector mBluetoothConnector;

    private Button scanButton;
    private BluetoothDevice [] pairedDevices = new BluetoothDevice[0];
    private String[] pairedStrings;

    private boolean connecting = false;

    private BTConnectCallback connectCallback;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bt_devices, container, false);
        scanButton = (Button) view.findViewById(R.id.button_scan);
        pairedListView = (ListView) view.findViewById(R.id.listview_paired);
        availableListView = (ListView) view.findViewById(R.id.listview_available);

        connectCallback = new BTConnectCallback() {
            @Override
            public void deviceConnected(final BluetoothSocket s) {
                DeviceManager.getInstance().setBluetoothService(s);
                connecting = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppData.getInstance().setMessageText("Connected");
                        Toast.makeText(getContext(), "Connected to " + s.getRemoteDevice().getName(), Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void unableToConnect(final String message) {
                connecting = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Unable to connect. " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getPairedDevices();
        return view;

    }

    private void getPairedDevices() {
        pairedDevices = mBluetoothAdapter.getBondedDevices().toArray(pairedDevices);
        pairedStrings = new String[pairedDevices.length];
        Log.d(TAG, "There are " + pairedDevices.length + " devices paired");
        if (pairedDevices.length > 0) {
            // There are paired devices. Get the name and address of each paired device.
            int index = 0;
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                pairedStrings[index] = deviceName + " - " + deviceHardwareAddress;
                index++;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, pairedStrings);
        pairedListView.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(pairedListView);
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, ""+position);
                connectTo(pairedDevices[position]);
            }
        });
    }

    private void connectTo(BluetoothDevice bluetoothDevice) {
        if ( connecting ) {
            return;
        }
        connecting = true;
        Toast.makeText(getContext(), "Connecting to " + bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();

        DeviceManager.getInstance().setCurrentDevice(bluetoothDevice);
        mBluetoothConnector = new BluetoothConnector(bluetoothDevice, connectCallback);
        mBluetoothConnector.start();
    }




}
