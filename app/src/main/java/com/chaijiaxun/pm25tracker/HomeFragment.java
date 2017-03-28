package com.chaijiaxun.pm25tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.bluetooth.BTDisconnectCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BTPacket;
import com.chaijiaxun.pm25tracker.bluetooth.Device;
import com.chaijiaxun.pm25tracker.bluetooth.DeviceManager;
import com.chaijiaxun.pm25tracker.utils.AppData;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "APPHomeFragment";


    Button connectButton;
    Button setMicroclimateButton;
    Button syncTimeButton;
    RelativeLayout warningLayout;
    RelativeLayout deviceNameLayout;
    Button unlinkButton;
    TextView warningText;
    TextView deviceNameText;
    TextView phoneTimeText;

    AlertDialog.Builder builder;

    final Handler handler = new Handler();
    Runnable aliveCheck;


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
        setMicroclimateButton = (Button)v.findViewById(R.id.button_set_microclimate);
        syncTimeButton = (Button)v.findViewById(R.id.button_sync_time);
        warningLayout = (RelativeLayout) v.findViewById(R.id.layout_warning);
        deviceNameLayout = (RelativeLayout) v.findViewById(R.id.layout_last_device);
        unlinkButton = (Button) v.findViewById(R.id.button_unlink);
        warningText = (TextView) v.findViewById(R.id.text_warning);
        deviceNameText = (TextView) v.findViewById(R.id.text_device_name);

        phoneTimeText = (TextView) v.findViewById(R.id.text_phone_time);


        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, BTDeviceFragment.newInstance())
                        .addToBackStack("Bluetooth")
                        .commit();
            }
        });
        builder = new AlertDialog.Builder(getContext());


        setMicroclimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence colors[] = new CharSequence[] {"Indoors", "Outdoors"};

                builder.setTitle("Choose a microclimate");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        DeviceManager.getInstance().setMicroclimate(which);
                    }
                });

                builder.show();
            }
        });

        syncTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        unlinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceManager.getInstance().unsetCurrentDevice();
                refreshItems();
            }
        });

        refreshItems();
        DeviceManager.getInstance().setDisconnectCallback(new BTDisconnectCallback() {
            @Override
            public void deviceDisonnected() {
                refreshItems();
            }
        });



        aliveCheck = new Runnable() {
            @SuppressLint("InlinedApi")
            @Override
            public void run() {
                Log.d(TAG, "Alive Check");

                try {
                    //do your code here
                    Date currentTime = new Date();
                    long timeUnix = currentTime.getTime();
                    Log.d(TAG, timeUnix + "");
                    phoneTimeText.setText(DateFormat.getDateTimeInstance().format(currentTime));
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally {
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 1000);
                }
            }
        };

        aliveCheck.run();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
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
