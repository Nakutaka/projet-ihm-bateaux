package com.example.project.activities;

public class otherMainActivity {//extends AppCompatActivity implements LocationListener {
}
    /*private WeatherReportViewModel mWeatherReportViewModel;
    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_INCIDENT_LIST = "com.example.project.activities.INCIDENT_LIST";
    public static final String EXTRA_INCIDENT_CLOUD = "com.example.project.activities.INCIDENT_CLOUD";
    public static final String EXTRA_INCIDENT_CURRENT = "com.example.project.activities.INCIDENT_CURRENT";
    public static final String EXTRA_INCIDENT_FOG = "com.example.project.activities.INCIDENT_FOG";
    public static final String EXTRA_INCIDENT_HAIL = "com.example.project.activities.INCIDENT_HAIL";
    public static final String EXTRA_INCIDENT_OTHER = "com.example.project.activities.INCIDENT_OTHER";
    public static final String EXTRA_INCIDENT_RAIN = "com.example.project.activities.INCIDENT_RAIN";
    public static final String EXTRA_INCIDENT_STORM = "com.example.project.activities.INCIDENT_STORM";
    public static final String EXTRA_INCIDENT_TEMPERATURE = "com.example.project.activities.INCIDENT_TEMP";
    public static final String EXTRA_INCIDENT_TRANSPARENCY = "com.example.project.activities.INCIDENT_TRANSPARENCY";
    public static final String EXTRA_INCIDENT_WIND = "com.example.project.activities.INCIDENT_WIND";
    public long start;
    ArrayList<OverlayItem> items = new ArrayList<>();
    private TextView currentLocation;
    private MyLocationNewOverlay myPosition;
    private OverlayItem currentPositionOverlay;
    private MapView map;
    private final OnlineTileSourceBase seamarks = TileSourceFactory.OPEN_SEAMAP;
    private FrameLayout reportDetails;
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;
    IMapController mapController;
    boolean onScroll = false;
    /**
     * User Settings (default value for the time being)
     */
    /*private double zoom = 15.5;


    /**
     * GPS variables
     */
    /* GPS stuff */
    /*private Location lastLocation; // WARNING can be null
    private String GPS_LOG_TOKEN = "GPS-LOGS";
    private String providerName;
    private LocationManager locationManager;
    /* GPS Constant Permission */
    /*private static final int MY_PERMISSION_ACCESS_LOCATION = 10;
    /* GPS update config */
   /* private static final int MINIMUM_TIME = 5*1000;// 5s
    private static final int MINIMUM_DISTANCE = 1;// 1m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        mWeatherReportViewModel = new ViewModelProvider(this).get(WeatherReportViewModel.class);
        // Update the cached copy of the reports in the map overlays (method reference style)
        mWeatherReportViewModel.getWeatherReports().observe(this, this::setReports);

        currentLocation = findViewById(R.id.current_location);
        reportDetails = findViewById(R.id.frame_layout_report_details);
        currentLocation.setText("Loading ...");

        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportFormActivity.class);
            start = Calendar.getInstance().getTimeInMillis();
            intent.putExtra(ITypeParam.MAP_ACTIVITY_START, start);
            startActivityForResult(intent, NEW_REPORT_ACTIVITY_REQUEST_CODE);
        });

        findViewById(R.id.fab_erase).setOnClickListener(v -> {
            mWeatherReportViewModel.clearWeatherReports();
        });

        setupGPS();
        setupMap();
        arrayButton();
        recenter();
        //displayInfo(null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == NEW_REPORT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK &&
                lastLocation!=null) {

/**********retenter avec list au cas où marcherait sinon un par un marche aussi*****************/
            //List<Incident> incidents = data.getParcelableArrayListExtra(EXTRA_INCIDENT_LIST);
            //List<Incident> incidents = new ArrayList<>();
        /*    Report report = new Report(start, lastLocation.getLatitude(), lastLocation.getLongitude());
            mWeatherReportViewModel.insert(report);
            Toast.makeText(
                    getApplicationContext(),"Report: " + report.reportId + " added!",
                    Toast.LENGTH_SHORT).show();

            Incident tmp;
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_CLOUD))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_CURRENT))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_FOG))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_HAIL))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_OTHER))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_RAIN))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_STORM))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_TEMPERATURE))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_TRANSPARENCY))!=null) insertIncident(tmp);
            if((tmp = data.getParcelableExtra(EXTRA_INCIDENT_WIND))!=null) insertIncident(tmp);
        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    "No report",
                    Toast.LENGTH_SHORT).show();
        }*/
   // }

    /*private void insertIncident(Incident newInc){
        long rId = start;
        int iId = newInc.incidentId;
        String com = newInc.comment;
        switch(iId) {
            case ITypeIncident.INCIDENT_TEMPERATURE: {
                Temperature i = new Temperature(rId, iId, com);
                toastyIncident("Temperature", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_RAIN: {
                Rain i = new Rain(rId, iId, com);
                toastyIncident("Rain", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_HAIL: {
                Hail i = new Hail(rId, iId, com);
                toastyIncident("Hail", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_FOG: {
                Fog i = new Fog(rId, iId, com);
                toastyIncident("Fog", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_CLOUD: {
                Cloud i = new Cloud(rId, iId, com);
                toastyIncident("Cloud", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_STORM: {
                Storm i = new Storm(rId, iId, com);
                toastyIncident("Storm", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_WIND: {
                Wind i = new Wind(rId, iId, com);
                toastyIncident("Wind", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_CURRENT: {
                Current i = new Current(rId, iId, com);
                toastyIncident("Current", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            case ITypeIncident.INCIDENT_TRANSPARENCY: {
                Transparency i = new Transparency(rId, iId, com);
                toastyIncident("Transparency", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
                break;
            }
            default: {
                Other i = new Other(rId, iId, com);
                toastyIncident("Other", i.incidentId, i.comment);
                mWeatherReportViewModel.insert(i);
            }
        }
    }

    private void toastyIncident(String type, int id, String com) {
        Toast.makeText(
                getApplicationContext(), type + " - " + id + " - " + com,
                Toast.LENGTH_SHORT).show();
    }

    void displayReportDetailsFragment() {
        ReportDetailsFragment details = new ReportDetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_report_details, details)
                .commit();
    }

    //link with setReports --> to trigger Fragment ReportDetailsFragment
    private void displayOverlays() {
        map.getOverlays().remove(mOverlay);
        mOverlay = new ItemizedOverlayWithFocus<>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        currentLocation.setText(item.getPoint().getLatitude() + " " + item.getPoint().getLongitude());
                        if (item.equals(currentPositionOverlay))
                            switchCoordinatesVisibility();
                        else {
                            currentLocation.setVisibility(View.VISIBLE);
                        }
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
                onScroll = false;
            }
        });
    }

    void recenter() {
        if (lastLocation != null)
            mapController.animateTo(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
        mapController.setZoom(zoom);
    }

    void switchCoordinatesVisibility() {
        if (currentLocation.isShown())
            currentLocation.setVisibility(View.INVISIBLE);
        else
            currentLocation.setVisibility(View.VISIBLE);
    }

    private void setReports(List<ReportWithIncidents> reports) {
        List<OverlayItem> reportItems = new ArrayList<>();
        reports.forEach(r -> {
            Report report = r.report;
            List<Incident> incidents = new ArrayList<>();
            if(r.cloudIncident!=null) incidents.add(r.cloudIncident);
            if(r.currentIncident!=null) incidents.add(r.currentIncident);
            if(r.fogIncident!=null) incidents.add(r.fogIncident);
            if(r.hailIncident!=null) incidents.add(r.hailIncident);
            if(r.otherIncident!=null) incidents.add(r.otherIncident);
            if(r.rainIncident!=null) incidents.add(r.rainIncident);
            if(r.stormIncident!=null) incidents.add(r.stormIncident);
            if(r.temperatureIncident!=null) incidents.add(r.temperatureIncident);
            if(r.transparencyIncident!=null) incidents.add(r.transparencyIncident);
            if(r.windIncident!=null) incidents.add(r.windIncident);
            int hour = (int)((report.reportId/(1000*60*60))%24);
            int min = (int)((report.reportId/(1000*60))%60);
            int sec = (int)(report.reportId/1000)%60;
            StringBuilder sbuf = new StringBuilder();
            Formatter fmt = new Formatter(sbuf);
            String time = fmt.format("%02dh%02dmin%02dsec", hour, min, sec).toString();
            //String time = String.format("%02d:%02d:%02d", hour, min, sec);
            AtomicReference<String> typeCom = new AtomicReference<>("");
            incidents.forEach(i -> typeCom.updateAndGet(v -> v + "\n" + i.incidentId + ": " + i.comment));
            reportItems.add(new OverlayItem("Report: " + time + " - " + incidents.size()
                    + (incidents.size()>1? " incidents" : " incident"), ""+typeCom,
                    new GeoPoint(report.latitude, report.longitude)));
        });
        items = new ArrayList<>(reportItems);
        updateMap();
    }

    private void updateMap() {
        //items = new ArrayList<OverlayItem>();
        if (lastLocation != null) {
            //mapController.setZoom(zoom);
            GeoPoint mapCenter = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
            //mapController.setCenter(mapCenter);
            items.remove(currentPositionOverlay);
            currentPositionOverlay = new OverlayItem("", "", mapCenter);
            items.add(currentPositionOverlay);
            displayOverlays();
        }
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

    /*@Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lastLocation = location;
            currentLocation.setText(location.getLatitude() + " " + location.getLongitude());
            if(!onScroll) //To know if the user was browsing the map.
                recenter();// --> otherwise not possible to navigate on the map
            Log.d(GPS_LOG_TOKEN,"Position updated");
            updateMap();
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



    /////////////////////////////////////////////////:
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
        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                onScroll= true;
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                onScroll= true;
                return false;
            }
        });
    }

    /**
     * GPS permissions setup
     */

    /*private void setupGPS() {
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
    /*private void sendUserToLocationSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(otherMainActivity.this);

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
    }*/
//}
