package com.chaijiaxun.pm25tracker;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int PERMISSION_REQUEST_LOCATION = 1;
    private static final String TAG = "APPMainActivity";
    GPSTracker tracker;
    double lat, lon;

    EditText sensorReading;
    ListView readingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
//        tracker = new GPSTracker(this);
//        if (!tracker.canGetLocation()) {
//            tracker.showSettingsAlert();
//        } else {
//            lat = tracker.getLatitude();
//            lon = tracker.getLongitude();
//            Log.d("MainActivity", lat + " " + lon);
//        }

        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//                args.putInt(HomeFragment.ARG_PLANET_NUMBER, position);
//                fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        Log.d(TAG, "Pressed home");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "Navigation Item Selected");
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (id) {
            case R.id.nav_home:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, HomeFragment.newInstance())
                        .addToBackStack("Home")
                        .commit();

                setTitle("PM2.5 App");

                Log.d(TAG, "Pressed home");
                break;
            case R.id.nav_stats:
                setTitle("Statistics");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, StatsFragment.newInstance())
                        .addToBackStack("Stats")
                        .commit();

                break;
            case R.id.nav_map:
                setTitle("Map");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, MapHistoryFragment.newInstance("123", "123123"))
                        .addToBackStack("Map")
                        .commit();

                break;
            case R.id.nav_readings:
                setTitle("Readings");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, ReadingsFragment.newInstance())
                        .addToBackStack("Readings")
                        .commit();

                break;
            case R.id.nav_settings:
                setTitle("Settings");

                break;
            case R.id.nav_bluetooth:
                setTitle("Bluetooth Devices");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, BTDeviceFragment.newInstance())
                        .addToBackStack("Bluetooth")
                        .commit();
                break;
            case R.id.nav_help:
                setTitle("Help and Feedback");

                break;

            case R.id.nav_dev:
                setTitle("Developer page");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, DevFragment.newInstance())
                        .addToBackStack("Dev")
                        .commit();

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
