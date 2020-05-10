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
import android.net.Uri;
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
import androidx.fragment.app.Fragment;

import com.example.project.R;

/**
 * This fragment is able to manage authorization for location module (GPS)
 * It can get the GPS position in a LatLng
 * It can get the placeName to the GPS position (nearest city)
 * It can force to display a placeName in the TextView
 */
public class GPSFragment extends Fragment implements LocationListener {
    private IGPSFragment igpsFragment; // able to recenter camera
    private TextView locationView;
    private Location currentLocation;
    LocationManager locationManager;

    // Overlay icons
    private ImageView imgLocationOff;


    /*******************GPS variables*******************/
    /* GPS stuff */
    private static final int MINIMUM_TIME = 1000; // 5*1000;// 5s
    private static final int MINIMUM_DISTANCE = 1;// 1m
    private String provider = LocationManager.GPS_PROVIDER; //LocationManager.NETWORK_PROVIDER


    public GPSFragment() { }
    public GPSFragment(IGPSFragment activity) { igpsFragment = activity; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (igpsFragment ==null) return super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        locationView = rootView.findViewById(R.id.locationView);
        imgLocationOff = rootView.findViewById(R.id.imgLocationOff);

        //Duct Tape
        imgLocationOff.setVisibility(View.INVISIBLE);

        setupGPS();

        imgLocationOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptLocationSettingOff();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(IGPSFragment.GPS_LOG_TOKEN,"Goodbye");
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocationIcon();
        if (!anyDialogDisplayed()) setupGPS();
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
            }, IGPSFragment.MY_PERMISSION_ACCESS_LOCATION);
            Log.d(IGPSFragment.GPS_LOG_TOKEN,"Requesting permissions");
            return;
        } else {
            // Permission already granted explicitly
            locationManager.requestLocationUpdates(provider, MINIMUM_TIME, MINIMUM_DISTANCE, this);
        }

        String providerName = locationManager.getBestProvider(criteria, true);
        Log.d(IGPSFragment.GPS_LOG_TOKEN,"provider="+ providerName);
    }

    ////////////////////////////////////////////////////////////////////////

    /******************* LocationListener interface *******************/

    @Override
    public void onLocationChanged(Location location) {
        boolean firstUpdate = currentLocation==null;
        currentLocation = location;
        Log.d("SCROLL","UPDATE");
        igpsFragment.focus(firstUpdate); // needs new location

        locationView.setText(String.format("%s - %s", currentLocation.getLatitude(), currentLocation.getLongitude()));

        if (location != null) {
            currentLocation = location;
            //currentLocationTextView.setText(location.getLatitude() + " - " + location.getLongitude());
            Log.d(IGPSFragment.GPS_LOG_TOKEN,"Position updated");
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
        Log.d(IGPSFragment.GPS_LOG_TOKEN, "status: "+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(IGPSFragment.GPS_LOG_TOKEN, "location on");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(IGPSFragment.GPS_LOG_TOKEN, "location off");
        promptLocationSettingOff();
    }

    private void updateLocationIcon() {
        imgLocationOff.setVisibility( (locationManager.isProviderEnabled(provider)) ? View.INVISIBLE : View.VISIBLE);
    }

    ////////////////////////////////////////////////////////////////////

    /*************************GPS permissions & setup***************************/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == IGPSFragment.MY_PERMISSION_ACCESS_LOCATION) {
            onLocationPermissionResult(grantResults);
        }
    }

    private void onLocationPermissionResult(int[] grantResults) {
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(IGPSFragment.GPS_LOG_TOKEN,"Permissions granted");

            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Double check because of Android being picky
                locationManager.requestLocationUpdates(provider, MINIMUM_TIME, MINIMUM_DISTANCE, this);
            }
        } else {
            Log.d(IGPSFragment.GPS_LOG_TOKEN,"Permissions denied");
            //currentLocationTextView.setText("Unknown\n(Permission denied)");
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(),"Location access required for position", Toast.LENGTH_SHORT).show();
            } else {
                // Option 'Don't ask again' has been checked before
                promptGPSrequired();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////



    /********************* User redirection ****************************/

    private void sendUserToLocationSettings(String msg) {
        // Send user to GPS settings
        if (msg!=null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    private void redirectToAppSettings(String msg) {
        //redirect user to app Settings
        if (msg!=null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }

    /********************* Dialogs ****************************/

    private boolean locationPromptAlreadyDisplayed = false;
    private boolean gpsPromptAlreadyDisplayed = false;

    private boolean anyDialogDisplayed() { return locationPromptAlreadyDisplayed || gpsPromptAlreadyDisplayed; }

    private void promptLocationSettingOff() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder
                .setTitle("Please enable location")
                .setMessage("This application needs location to retrieve your position")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Close dialog
                        sendUserToLocationSettings("Please enable location");
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Proposition rejected", Toast.LENGTH_SHORT).show();
                        updateLocationIcon();
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                locationPromptAlreadyDisplayed = true;
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                locationPromptAlreadyDisplayed = false;
            }
        });

        // show it
        if (!locationPromptAlreadyDisplayed) alertDialog.show();
    }

    private void promptGPSrequired() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder
                .setTitle("Location permission needed")
                .setMessage("This application won't be able to operate normally unless it has the location permission")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Close dialog
                        redirectToAppSettings("Please give this app Location permission");                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                gpsPromptAlreadyDisplayed = true;
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                gpsPromptAlreadyDisplayed = false;
            }
        });

        // show it
        if (!gpsPromptAlreadyDisplayed) alertDialog.show();
    }
    ///////////////////////////////////////////////////////////////////////////////

    // ------------------------------------------------------------------------------

}