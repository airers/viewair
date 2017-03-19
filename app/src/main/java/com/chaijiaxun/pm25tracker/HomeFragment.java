package com.chaijiaxun.pm25tracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.bluetooth.Device;
import com.chaijiaxun.pm25tracker.bluetooth.DeviceManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "APPHomeFragment";


    Button connectButton;
    RelativeLayout warningLayout;
    RelativeLayout deviceNameLayout;
    Button unlinkButton;
    TextView warningText;
    TextView deviceNameText;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        connectButton = (Button)v.findViewById(R.id.button_connect);
        warningLayout = (RelativeLayout) v.findViewById(R.id.layout_warning);
        deviceNameLayout = (RelativeLayout) v.findViewById(R.id.layout_last_device);
        unlinkButton = (Button) v.findViewById(R.id.button_unlink);
        warningText = (TextView) v.findViewById(R.id.text_warning);
        deviceNameText = (TextView) v.findViewById(R.id.text_device_name);


        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceManager.getInstance().setCurrentDevice(new Device());
                refreshItems();

            }
        });

        unlinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceManager.getInstance().setCurrentDevice(null);
                refreshItems();
            }
        });


        refreshItems();

        return v;
    }

    /**
     * Does the hide / show logic for the page
     */
    public void refreshItems() {
        deviceNameLayout.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.INVISIBLE);
        connectButton.setVisibility(View.INVISIBLE);

        if ( DeviceManager.getInstance().hasLastDevice() ) {
            deviceNameLayout.setVisibility(View.VISIBLE);
            deviceNameText.setText(DeviceManager.getInstance().getCurrentDevice().getName());
            if ( DeviceManager.getInstance().isDeviceConnected() ) {
                warningLayout.setVisibility(View.VISIBLE);
                warningText.setText("DEVICE NOT CONNECTED");
            }
        } else {
            warningLayout.setVisibility(View.VISIBLE);
            warningText.setText("NO DEVICE CONNECTED");
            connectButton.setVisibility(View.VISIBLE);
        }


    }


}
