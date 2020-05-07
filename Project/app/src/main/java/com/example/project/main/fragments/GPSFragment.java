package com.example.project.main.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * This fragment is able to manage autorisation for location module (GPS)
 * It can get the GPS position in a LatLng
 * It can get the placeName to the GPS position (nearest city)
 * It can force to display a placeName in the TextView
 */
public class GPSFragment extends Fragment implements LocationListener {
    private IGPSActivity igpsActivity; // able to recenter camera
    private TextView locationView;
    private Location currentLocation;
    LocationManager locationManager;

    // Overlay icons
    private ImageView imgGPSoff;
    private ImageView imgLocationOff;


    /*******************GPS variables*******************/
    /* GPS stuff */
    private static final int MINIMUM_TIME = 1000; // 5*1000;// 5s
    private static final int MINIMUM_DISTANCE = 1;// 1m


    public GPSFragment() { }
    public GPSFragment(IGPSActivity activity) { igpsActivity = activity; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (igpsActivity==null) return super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        locationView = rootView.findViewById( R.id.locationView );
        imgGPSoff = rootView.findViewById( R.id.imgGPSoff );
        imgLocationOff = rootView.findViewById( R.id.imgLocationOff );

        //Duct Tape
        imgGPSoff.setVisibility( View.INVISIBLE );
        imgLocationOff.setVisibility( View.INVISIBLE );

        setupGPS();

        imgLocationOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeLocationSettings();
            }
        });

        imgGPSoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeLocationPermission();
            }
        });


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //locationManager.removeUpdates(this);
        Log.d(IGPSActivity.GPS_LOG_TOKEN,"Goodbye");
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupGPS();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    /*************************GPS setup***************************/
    private void setupGPS() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define minimum criteria for location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false); // Need direction ?
        criteria.setCostAllowed(false);
        criteria.setSpeedRequired(false); // Need speed ?

        //SDK_INT is always >= 26
        // Need to ask explicitly for permission access
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted yet

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, IGPSActivity.MY_PERMISSION_ACCESS_LOCATION);
            Log.d(IGPSActivity.GPS_LOG_TOKEN,"Requesting permissions");
            return;
        } else {
            // Permission already granted explicitly
            setUpLocationUpdates();
        }

        String providerName = locationManager.getBestProvider(criteria, true);
        Log.d(IGPSActivity.GPS_LOG_TOKEN,"provider="+ providerName);
    }

    ////////////////////////////////////////////////////////////////////////

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void setUpLocationUpdates() {
        //locationManager.requestLocationUpdates("gps", MINIMUM_TIME, MINIMUM_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME, 0, MINIMUM_DISTANCE);//locationListener);
        //LocationManager.NETWORK_PROVIDER --> when GPS not working BUT need wifi/network connection
    }
    /******************* LocationListener interface *******************/

    @Override
    public void onLocationChanged(Location location) {
        if (currentLocation==null) {
            Log.d(IGPSActivity.GPS_LOG_TOKEN,"first location change");
            igpsActivity.focus(true);
        }

        currentLocation = location;
        igpsActivity.focus(false); // recentrer

        locationView.setText(String.format("%s %s",currentLocation.getLatitude(), currentLocation.getLongitude()));

        if (location != null) {
            currentLocation = location;
            //currentLocationTextView.setText(location.getLatitude() + " - " + location.getLongitude());
            Log.d(IGPSActivity.GPS_LOG_TOKEN,"Position updated");
            //updateMap();
        }
        else {
            Toast.makeText(
                    getContext(),
                    "onLocationChanged triggered BUT got a null location...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(IGPSActivity.GPS_LOG_TOKEN, "status: "+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(IGPSActivity.GPS_LOG_TOKEN, "location on");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(IGPSActivity.GPS_LOG_TOKEN, "location off");
        promptLocationSettingOff();
    }

    ////////////////////////////////////////////////////////////////////

    /*************************GPS permissions & setup***************************/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == IGPSActivity.MY_PERMISSION_ACCESS_LOCATION) {
            onLocationPermissionResult(grantResults);
        }
    }

    private void onLocationPermissionResult(int[] grantResults) {
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(IGPSActivity.GPS_LOG_TOKEN,"Permissions granted");

            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Double check because of Android being picky
                setUpLocationUpdates();
            }
        } else {
            Log.d(IGPSActivity.GPS_LOG_TOKEN,"Permissions denied");
            //currentLocationTextView.setText("Unknown\n(Permission denied)");
            Toast.makeText(getContext(),"Location access required\n for position", Toast.LENGTH_SHORT).show();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////



    /********************* User redirection ****************************/

    private void sendUserToLocationSettings() {
        // Send user to GPS settings
        Toast.makeText(getContext(), "Please enable location", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    /*********************Prompts a dialog to go to location settings****************************/

    private void promptLocationSettingOff() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());//MainActivity.this);
        // set title
        alertDialogBuilder.setTitle("Please enable location");
        // set dialog message
        alertDialogBuilder
                .setMessage("This application needs location to retrieve your position")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Close dialog
                        sendUserToLocationSettings();
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Proposition rejected", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    ///////////////////////////////////////////////////////////////////////////////

    // ------------------------------------------------------------------------------

}
