package com.example.projet.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet.R;
import com.example.projet.not_used_for_now_core.IReport;
import com.example.projet.fragments.InfoBox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements LocationListener {
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    private TextView currentLocation;
    private MyLocationNewOverlay myPosition;
    private OverlayItem currentPositionOverlay;
    private MapView map;
    private final OnlineTileSourceBase seamarks = TileSourceFactory.OPEN_SEAMAP;
    private FrameLayout infoBox;
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;
    IMapController mapController;
    /**
     * User Settings (default value for the time being)
     */
    private double zoom = 15.5;


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
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        displayInfo(null);
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
            }
        });
        currentLocation = findViewById(R.id.current_location);
        infoBox = findViewById(R.id.infoBox);
        currentLocation.setText("Loading ...");
        setupGPS();
        setupMap();
        arrayButton();
    }

    private void displayOverlays() {
        map.getOverlays().remove(mOverlay);
        mOverlay = new ItemizedOverlayWithFocus<>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        if (item.equals(currentPositionOverlay))
                            swipeCoordinatesVisibility();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);

    }

    private void arrayButton() {
        ImageButton buttonRecenter = findViewById(R.id.fab_recenter);
        buttonRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recenter();
            }
        });
    }

    void recenter() {
        if (lastLocation != null)
            mapController.animateTo(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
        mapController.setZoom(zoom);
    }

    void swipeCoordinatesVisibility() {
        if (currentLocation.isShown())
            currentLocation.setVisibility(View.INVISIBLE);
        else
            currentLocation.setVisibility(View.VISIBLE);
    }

    private void setupMap() {
        map = findViewById(R.id.map_view);
        mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBackgroundColor(Color.TRANSPARENT);
        final MapTileProviderBase tileProvider = new MapTileProviderBasic(this, seamarks);
        final TilesOverlay seamarksOverlay = new TilesOverlay(tileProvider, this);
        seamarksOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        map.getOverlays().add(seamarksOverlay);
        myPosition = new MyLocationNewOverlay(map);
        myPosition.enableFollowLocation();
        map.getOverlays().add(myPosition); //User's position on the map
    }

    private void updateMap() {
        if (lastLocation != null) {
            mapController.setZoom(zoom);
            GeoPoint mapCenter = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
            mapController.setCenter(mapCenter);
            items.remove(currentPositionOverlay);
            currentPositionOverlay = new OverlayItem("", "", mapCenter);
            items.add(currentPositionOverlay);
            displayOverlays();
        }
    }



    void displayInfo(IReport signalement) {
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
            recenter();
            Log.d(GPS_LOG_TOKEN,"Position updated");
            updateMap();
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

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up

    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

}
