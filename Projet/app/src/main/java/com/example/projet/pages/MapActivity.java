package com.example.projet.pages;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet.R;
import com.example.projet.core.ISignalement;
import com.example.projet.pages.fragments.InfoBox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements LocationListener {

    private TextView currentLocation;

    /**
     * GPS variables
     */
    /* GPS stuff */
    private Location lastLocation; // WARNING can be null
    private String GPS_LOG_TOKEN = "GPS-LOGS";
    private String providerName;
    private LocationManager locationManager;
    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_LOCATION = 10;
    /* GPS update config */
    private static final int MINIMUM_TIME = 5*1000;  // 5s
    private static final int MINIMUM_DISTANCE = 1; // 1m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        displayInfo(null);
        findViewById(R.id.bouton_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignalementActivity.class);
                startActivity(intent);
            }
        });

        currentLocation = findViewById(R.id.currentLocation);
        currentLocation.setText("Loading ...");

        setupGPS();
    }

    void displayInfo(ISignalement signalement) {
        InfoBox infoBox = new InfoBox();
        getSupportFragmentManager().beginTransaction().replace(R.id.infoBox, infoBox).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        Log.d(GPS_LOG_TOKEN,"Goodbye");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_LOCATION:
                onLocationPermissionResult(grantResults);
                break;

        }
    }

    /**
     *  LocationListener interface
     */

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lastLocation = location;
            currentLocation.setText(location.getLatitude() + " " + location.getLongitude());
            Log.d(GPS_LOG_TOKEN,"Position updated");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        // When GPS disabled
        sendUserToLocationSettings();
    }

    /**
     * GPS permissions setup
     */

    private void setupGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // When GPS disabled
            sendUserToLocationSettings();
        }

        // Define minimum criteria for location provider
        Criteria critere = new Criteria();
        critere.setAccuracy(Criteria.ACCURACY_COARSE);
        critere.setAltitudeRequired(false);
        critere.setBearingRequired(false); // Need direction ?
        critere.setCostAllowed(false); // Needs payment (ex:google?) ?
        critere.setSpeedRequired(false); // Need speed ?


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Need to ask explicitly for permission access

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted yet

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, MY_PERMISSION_ACCESS_LOCATION);
                Log.d(GPS_LOG_TOKEN,"Requesting permissions");
                return;
            } else {
                // Permission already granted explicitly
                setUpLocationUpdates();
            }
        } else {
            // Permission access already granted implicitly (just manifest declaration)

            Log.d(GPS_LOG_TOKEN,"No need to ask for permissions");
            setUpLocationUpdates();
        }

        providerName = locationManager.getBestProvider(critere, true);
        Log.d(GPS_LOG_TOKEN,"provider="+providerName);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void setUpLocationUpdates() {
        locationManager.requestLocationUpdates("gps", MINIMUM_TIME, MINIMUM_DISTANCE, this);
    }

    void onLocationPermissionResult(int[] grantResults) {
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(GPS_LOG_TOKEN,"Permissions granted");

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Double check because of Android being picky
                setUpLocationUpdates();
            }
        } else {
            Log.d(GPS_LOG_TOKEN,"Permissions denied");
            currentLocation.setText("Unknown\n(Permission denied)");
            Toast.makeText(getApplicationContext(),"Location access required\n for position",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Prompts a dialog to go to location settings
     */
    private void sendUserToLocationSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);

        // set title
        alertDialogBuilder.setTitle("Please enable location");

        // set dialog message
        alertDialogBuilder
                .setMessage("This application needs location to retrieve your position")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Sending you to settings ...", Toast.LENGTH_SHORT).show();
                        dialog.cancel(); // Close dialog

                        // Send user to GPS settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Proposition rejected", Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
