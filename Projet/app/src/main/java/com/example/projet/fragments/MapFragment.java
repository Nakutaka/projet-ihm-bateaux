package com.example.projet.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

import com.example.projet.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements LocationListener {

    private Activity mainActivity;

    //List<OverlayItem> items;// = new ArrayList<>();
    private TextView currentLocationTextView;
    private MyLocationNewOverlay myPosition;
    private MapView map;
    private final OnlineTileSourceBase seaMarks = TileSourceFactory.OPEN_SEAMAP;
    //private FrameLayout reportDetails;
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;
    IMapController mapController;
    boolean onScroll = false;

    /**********User Settings (default value for the time being)*********/
    private double zoom = 15.5;

    /*******************GPS variables*******************/
    /* GPS stuff */
    private Location currentLocation; // WARNING can be null
    private String GPS_LOG_TOKEN = "GPS-LOGS";
    private LocationManager locationManager;
    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_LOCATION = 10;
    /* GPS update config */
    private static final int MINIMUM_TIME = 5*1000;// 5s
    private static final int MINIMUM_DISTANCE = 1;// 1m


    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = getActivity();
        /*Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));*/
        setupGPS();
        map = mainActivity.findViewById(R.id.map_view);
        currentLocationTextView = mainActivity.findViewById(R.id.current_location);
        currentLocationTextView.setText("Loading ...");
        setupMap();
        recenter();
    }



    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        //map = rootView.findViewById(R.id.map_view);
        //reportDetails = rootView.findViewById(R.id.frame_layout_report_details);

        /*rootView.findViewById(R.id.fab_rain).setOnClickListener(this);*/

      /*  return rootView;
    }*/

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);//this? or mainActivity.getContext()???
        Log.d(GPS_LOG_TOKEN,"Goodbye");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSION_ACCESS_LOCATION) {
                onLocationPermissionResult(grantResults);
        }
    }
    /*******************************************************************/

    /////////////////////////////////////////////////////////////////////

    ///use it could be good if user taps somewhere else on the map
    /*void switchCoordinatesVisibility() {
        if (currentLocation.isShown())
            currentLocation.setVisibility(View.INVISIBLE);
        else
            currentLocation.setVisibility(View.VISIBLE);
    }*/


    public double getCurrentLatitude() {
        return Objects.requireNonNull(currentLocation).getLatitude();
    }

    public double getCurrentLongitude() {
        return Objects.requireNonNull(currentLocation).getLongitude();
    }
    /********************************************************************/

    public void recenterButtonAction() {
        recenter();
        onScroll = false;
    }

    private void recenter() {
        if (currentLocation != null)
            mapController.animateTo(new GeoPoint(currentLocation.getLatitude(),
                    currentLocation.getLongitude()));
        mapController.setZoom(zoom);
    }

    public void updateMap(ItemizedOverlayWithFocus<OverlayItem> newOverlayItems) {
        map.getOverlays().remove(mOverlay);
        mOverlay = newOverlayItems;
        //mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }

    //////////////////////////////////////////////////////////////////////

    /*************************Map setup***************************/
    private void setupMap() {
        //map = mainActivity.findViewById(R.id.map_view);
        if(map!=null) mapController = map.getController();
        else {
            Toast.makeText(
                    mainActivity.getApplicationContext(),"pb!!!!!!!",
                    Toast.LENGTH_SHORT).show();
        }
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBackgroundColor(Color.TRANSPARENT);
        final MapTileProviderBase tileProvider = new MapTileProviderBasic(
                mainActivity.getApplicationContext(), seaMarks);
        final TilesOverlay seaMarksOverlay = new TilesOverlay(tileProvider,
                mainActivity.getApplicationContext());
        seaMarksOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        map.getOverlays().add(seaMarksOverlay);
        myPosition = new MyLocationNewOverlay(map);
        myPosition.enableFollowLocation();
        map.getOverlays().add(myPosition); //User's position on the map
        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                onScroll = true;
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                onScroll = true;
                return false;
            }
        });
    }
    ///////////////////////////////////////////////////////////////////
    /*************************GPS permissions & setup***************************/
    private void setupGPS() {
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // When GPS disabled
            sendUserToLocationSettings();
        }

        // Define minimum criteria for location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false); // Need direction ?
        criteria.setCostAllowed(false); // Needs payment (ex:google?) ?
        criteria.setSpeedRequired(false); // Need speed ?

        //Unnecessary; SDK_INT is always >= 26
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Need to ask explicitly for permission access

            if (mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || mainActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        /*} else {
            // Permission access already granted implicitly (just manifest declaration)

            Log.d(GPS_LOG_TOKEN,"No need to ask for permissions");
            setUpLocationUpdates();
        }*/

        String providerName = locationManager.getBestProvider(criteria, true);
        Log.d(GPS_LOG_TOKEN,"provider="+ providerName);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void setUpLocationUpdates() {
        locationManager.requestLocationUpdates("gps", MINIMUM_TIME, MINIMUM_DISTANCE, this);
    }

    private void onLocationPermissionResult(int[] grantResults) {
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(GPS_LOG_TOKEN,"Permissions granted");

            if (mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && mainActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Double check because of Android being picky
                setUpLocationUpdates();
            }
        } else {
            Log.d(GPS_LOG_TOKEN,"Permissions denied");
            currentLocationTextView.setText("Unknown\n(Permission denied)");
            Toast.makeText(mainActivity.getApplicationContext(),"Location access required\n for position", Toast.LENGTH_SHORT).show();
        }
    }
    ////////////////////////////////////////////////////////////////////////

    /*********************Prompts a dialog to go to location settings****************************/
    private void sendUserToLocationSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());//MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Please enable location");

        // set dialog message
        alertDialogBuilder
                .setMessage("This application needs location to retrieve your position")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mainActivity.getApplicationContext(), "Sending you to settings ...", Toast.LENGTH_SHORT).show();
                        dialog.cancel(); // Close dialog

                        // Send user to GPS settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mainActivity.getApplicationContext(), "Proposition rejected", Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    ///////////////////////////////////////////////////////////////////////////////

    /************************ LocationListener interface*************************/
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
            currentLocationTextView.setText(location.getLatitude() + " - " + location.getLongitude());
            if(!onScroll) //To know if the user was browsing the map.
                recenter();// --> otherwise not possible to navigate on the map
            Log.d(GPS_LOG_TOKEN,"Position updated");
            //updateMap();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {
        // When GPS disabled
        sendUserToLocationSettings();
    }
/////////////////////////////////////////////////////////////////////////////
}
