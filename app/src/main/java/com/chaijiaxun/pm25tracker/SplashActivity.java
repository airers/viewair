package com.chaijiaxun.pm25tracker;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.HandsetInfo;
import com.chaijiaxun.pm25tracker.utils.TimezoneUtils;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppData.getInstance().init(getApplicationContext());
        
        HandsetInfo.init();
        TimezoneUtils.getPhoneTimezone();

        if ( !AppData.getInstance().acceptedEULA() ) {
            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
                    .setTitle("End User Licence Agreement")
                    .setMessage(getString(R.string.eula))
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AppData.getInstance().acceptEULA();
                            bluetoothCheck();
                        }
                    })
                    .show();
        } else {
            bluetoothCheck();
        }

    }

    protected void bluetoothCheck() {
        if ( !AppData.getInstance().getBluetoothAdapter().isEnabled() ) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            proceed();
        }
    }

    /**
     * Used for the return value of the system dialogue to enable bluetooth.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                proceed();
            } else if (resultCode == RESULT_CANCELED) {
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
                        .setTitle("Bluetooth Required")
                        .setMessage("Bluetooth could not be enabled. You need to enable bluetooth to connect to the device.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                proceed();
                            }
                        })
                        .show();
            }
        }

    }

    private void proceed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}