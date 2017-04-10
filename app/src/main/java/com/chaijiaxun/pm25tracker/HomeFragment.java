package com.chaijiaxun.pm25tracker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.bluetooth.BTDisconnectCallback;
import com.chaijiaxun.pm25tracker.bluetooth.BTPacket;
import com.chaijiaxun.pm25tracker.bluetooth.Device;
import com.chaijiaxun.pm25tracker.bluetooth.DeviceManager;
import com.chaijiaxun.pm25tracker.server.ServerDataManager;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.ByteUtils;
import com.jaredrummler.android.device.DeviceName;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "APPHomeFragment";


    Button connectButton;
    Button setMicroclimateButton, syncTimeButton, syncDataButton;
    RelativeLayout warningLayout;
    RelativeLayout deviceNameLayout;
    Button unlinkButton;
    TextView warningText;
    TextView deviceNameText;
    TextView phoneTimeText, deviceTimeText, syncTimeText, readingCountText;

    Button sendReadingsButton;
    TextView notSyncedTextView;
    TextView deviceIDTextView;

    ImageView connectionStatusImage;

    AlertDialog.Builder builder;

    final Handler deviceInfoUpdateHandler = new Handler();
    Runnable deviceInfoUpdateRunnable;


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

        connectionStatusImage = (ImageView) v.findViewById(R.id.image_connection_status);

        // Status card
        phoneTimeText = (TextView) v.findViewById(R.id.text_phone_time);
        deviceTimeText = (TextView) v.findViewById(R.id.text_device_time);
        syncTimeText = (TextView) v.findViewById(R.id.text_sync_time);
        readingCountText = (TextView) v.findViewById(R.id.text_reading_count);

        syncTimeText.setText(DateFormat.getDateTimeInstance().format(new Date(1490830040000L)));

        setMicroclimateButton = (Button)v.findViewById(R.id.button_set_microclimate);
        syncTimeButton = (Button)v.findViewById(R.id.button_sync_time);
        syncDataButton = (Button)v.findViewById(R.id.button_get_readings);


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
                if ( !DeviceManager.getInstance().isDeviceConnected() ) {
                    Toast.makeText(getContext(), "No device connected", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date currentTime = new Date();
                long timeUnix = currentTime.getTime() + 3; // The 3 seconds is to offset for network delay
                byte [] timeBytes = ByteUtils.androidLongTSToAndroidLongTS(timeUnix);

                byte [] bytes = new byte[6];
                bytes[0] = BTPacket.TYPE_SET_TIME;
                bytes[1] = 4;
                bytes[2] = timeBytes[0];
                bytes[3] = timeBytes[1];
                bytes[4] = timeBytes[2];
                bytes[5] = timeBytes[3];
                
                DeviceManager.getInstance().getBluetoothService().write(bytes);

                Toast.makeText(getContext(), "Syncing time", Toast.LENGTH_SHORT).show();
            }
        });

        syncDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !DeviceManager.getInstance().isDeviceConnected() ) {
                    Toast.makeText(getContext(), "No device connected", Toast.LENGTH_SHORT).show();
                    return;
                }
//                byte [] bytes = new byte[6];
//                bytes[0] = BTPacket.TYPE_GET_READINGS;
//                bytes[1] = 4;
//                byte [] timeBytes = ByteUtils.androidLongTSToAndroidLongTS(1490830040000L);
//                bytes[2] = timeBytes[0];
//                bytes[3] = timeBytes[1];
//                bytes[4] = timeBytes[2];
//                bytes[5] = timeBytes[3];
//
//                DeviceManager.getInstance().getBluetoothService().write(bytes);

                byte [] timeBytes = ByteUtils.androidLongTSToAndroidLongTS(0);
                byte [] bytes = new byte[8];
                bytes[0] = BTPacket.TYPE_GET_READINGS;
                bytes[1] = 4;
                bytes[2] = timeBytes[0];
                bytes[3] = timeBytes[1];
                bytes[4] = timeBytes[2];
                bytes[5] = timeBytes[3];

                DeviceManager.getInstance().getBluetoothService().write(bytes);
                Toast.makeText(getContext(), "Asking for readings", Toast.LENGTH_SHORT).show();
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


        deviceInfoUpdateRunnable = new Runnable() {
            @SuppressLint("InlinedApi")
            @Override
            public void run() {
                try {
                    //do your code here
                    Date currentTime = new Date();
                    long timeUnix = currentTime.getTime();

                    int timeInt = (int)(timeUnix / 1000);
//                    Log.d(TAG, timeInt + "");
                    byte [] timeBytes = ByteUtils.intToByteArray(timeInt);
//                    Log.d(TAG, ByteUtils.byteArrayToString(timeBytes));
                    phoneTimeText.setText(DateFormat.getDateTimeInstance().format(currentTime));
                    Device currentDevice = DeviceManager.getInstance().getCurrentDevice();
                    currentDevice.incrementSecond();
                    if ( currentDevice == null ) {
                        deviceTimeText.setText("No device connected");
                        readingCountText.setText("No device connected");
                    } else {
                        deviceTimeText.setText(DateFormat.getDateTimeInstance().format(currentDevice.getDeviceTime()));
                        readingCountText.setText(""+DeviceManager.getInstance().getPendingReadings());
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally {
                    //also call the same runnable to call it at regular interval
                    deviceInfoUpdateHandler.postDelayed(deviceInfoUpdateRunnable, 1000);
                }
            }
        };

        deviceInfoUpdateHandler.post(deviceInfoUpdateRunnable);


        sendReadingsButton = (Button)v.findViewById(R.id.button_send_readings);
        deviceIDTextView = (TextView)v.findViewById(R.id.text_device_id);
        notSyncedTextView = (TextView)v.findViewById(R.id.text_not_synced_count);

        ServerDataManager.getLatestTime(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // called when response HTTP status is "200 OK"
                String sb = new String(responseBody);
                notSyncedTextView.setText(sb);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



        DeviceName.with(getContext()).request(new DeviceName.Callback() {

            @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                String androidID = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String manufacturer = info.manufacturer;  // "Samsung"
                String name = info.marketName;            // "Galaxy S7 Edge"
                String model = info.model;                // "SAMSUNG-SM-G935A"
                String codename = info.codename;          // "hero2lte"
                String deviceName = info.getName();       // "Galaxy S7 Edge"
                int androidSDKVersion = android.os.Build.VERSION.SDK_INT;
                String androidVersionRelease = android.os.Build.VERSION.RELEASE;

                StringBuilder deviceInfo = new StringBuilder();
                deviceInfo.append("Android ID: ");
                deviceInfo.append(androidID);
                deviceInfo.append('\n');
                deviceInfo.append("SDK: ");
                deviceInfo.append(androidSDKVersion);
                deviceInfo.append('\n');
                deviceInfo.append("Release: ");
                deviceInfo.append(androidVersionRelease);
                deviceInfo.append('\n');
                deviceInfo.append("Manufacturer: ");
                deviceInfo.append(manufacturer);
                deviceInfo.append('\n');
                deviceInfo.append("Market Name: ");
                deviceInfo.append(name);
                deviceInfo.append('\n');
                deviceInfo.append("Model Name: ");
                deviceInfo.append(model);
                deviceInfo.append('\n');
                deviceInfo.append("Codename: ");
                deviceInfo.append(codename);
                deviceInfo.append('\n');
                deviceInfo.append("Device Name: ");
                deviceInfo.append(deviceName);
                deviceIDTextView.setText(deviceInfo.toString());
            }
        });


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
        connectionStatusImage.setImageResource(R.drawable.connection_none);

        if ( DeviceManager.getInstance().hasLastDevice() ) {
            deviceNameLayout.setVisibility(View.VISIBLE);
            deviceNameText.setText(DeviceManager.getInstance().getCurrentDevice().getName());
            if ( DeviceManager.getInstance().isDeviceConnected() ) {
                connectionStatusImage.setImageResource(R.drawable.connection_good);
            }
        }
        else {
            warningLayout.setVisibility(View.VISIBLE);
            warningText.setText("NO DEVICE CONNECTED");
            connectButton.setVisibility(View.VISIBLE);
        }


    }


}
