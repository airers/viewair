package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.SettingType;
import com.chaijiaxun.pm25tracker.utils.TimezoneUtils;

/**
 * Settings for the App
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    Button buttonSave, buttonCancel;

    SwitchCompat switchAutoconnect, switchAutoconnectDevice, swtichAutosync,
                 switchStatusbar, switchCloud;

    int selectedTimezoneOffset = 0;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonCancel = (Button)view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSettings();
            }
        });
        buttonSave = (Button)view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        switchAutoconnect = (SwitchCompat) view.findViewById(R.id.switch_setting_autoconnect);
        switchAutoconnectDevice = (SwitchCompat) view.findViewById(R.id.switch_setting_autoconnect_device);
        swtichAutosync = (SwitchCompat) view.findViewById(R.id.switch_setting_auto_sync);
        switchStatusbar = (SwitchCompat) view.findViewById(R.id.switch_setting_status);
        switchCloud = (SwitchCompat) view.findViewById(R.id.switch_setting_cloud);


        readSettings();

        Spinner timezoneSpinner = (Spinner) view.findViewById(R.id.spinner_timezone);

        String[] timezones = TimezoneUtils.getNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timezones);
        timezoneSpinner.setAdapter(adapter);

        timezoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedTimezoneOffset = TimezoneUtils.getTimezoneOffset(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }
        });

        int spinnerIndex = TimezoneUtils.indexOfTimezone(selectedTimezoneOffset);
        if ( spinnerIndex >= 0 ) {
            timezoneSpinner.setSelection(spinnerIndex);
        }

        return view;
    }

    /**
     * Reads the settings and sets the switches
     */
    public void readSettings() {
        switchAutoconnect.setChecked(AppData.getInstance().getSetting(SettingType.AUTOCONNECT));
        switchAutoconnectDevice.setChecked(AppData.getInstance().getSetting(SettingType.AUTOCONNECT_DEVICE));
        swtichAutosync.setChecked(AppData.getInstance().getSetting(SettingType.AUTOSYNC));
        switchStatusbar.setChecked(AppData.getInstance().getSetting(SettingType.STATUSBAR));
        switchCloud.setChecked(AppData.getInstance().getSetting(SettingType.CLOUD));
        selectedTimezoneOffset = AppData.getInstance().getTimezoneOffset();
    }

    public void saveSettings() {
        AppData.getInstance().saveSetting(SettingType.AUTOCONNECT, switchAutoconnect.isChecked());
        AppData.getInstance().saveSetting(SettingType.AUTOCONNECT_DEVICE, switchAutoconnectDevice.isChecked());
        AppData.getInstance().saveSetting(SettingType.AUTOSYNC, swtichAutosync.isChecked());
        AppData.getInstance().saveSetting(SettingType.STATUSBAR, switchStatusbar.isChecked());
        AppData.getInstance().saveSetting(SettingType.CLOUD, switchCloud.isChecked());
        AppData.getInstance().setTimezoneOffset(selectedTimezoneOffset);
        Toast.makeText(getContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
    }
}
