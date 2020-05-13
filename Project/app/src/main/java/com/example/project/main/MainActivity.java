package com.example.project.main;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.control.WeatherReportViewModel;
import com.example.project.database.remote.RetrofitInstance;
import com.example.project.database.remote.WebService;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;
import com.example.project.model.unused.Date;
import com.example.project.model.weather.Report;
import com.example.project.model.weather.WeatherReport;
import com.example.project.model.weather.local.Incident;
import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.example.project.model.weather.remote.RemoteIncident;
import com.example.project.model.weather.remote.RemoteWeatherReport;
import com.example.project.types.ITypeIncident;
import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPORT = "com.example.project.main.activities.REPORT";
    public static final String EXTRA_INCIDENT_MIN_LIST = "com.example.project.main.activities.INCIDENT_MIN_LIST";
    public static final String EXTRA_INCIDENT_BASIC_LIST = "com.example.project.main.activities.INCIDENT_BASIC_LIST";
    public static final String EXTRA_INCIDENT_MEASURED_LIST = "com.example.project.main.activities.INCIDENT_MEASURED_LIST";
    private Gson gson = new Gson();
    private SettingsViewModel svm;
    private MapFragment mapFragment;
    ReportDetailsFragment detailsFragment;
    private WeatherReportViewModel mWeatherReportViewModel;
    private WebService service;
    private FragmentManager fm;
    private List<WeatherReport> reportsNewlyRetrievedFromDB;
    private List<WeatherReport> reportsNotSentYet;
    /*private final String android_id = "device";/*Settings.Secure.getString(MainActivity.this.getContentResolver(),
            Settings.Secure.ANDROID_ID);//better than nothing*/
    private String deviceId;
    private boolean displayNotifs;
    //String device_unique_id;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        svm = new SettingsViewModel(getApplication());
        svm.getSettingsModelMutableLiveData().observe(this, newSettings -> {
            displayCoordinates(newSettings.isDisplayCoordinatesOn());
            displayNotifs = newSettings.isIncidentNotificationOn();
        });
        reportsNewlyRetrievedFromDB = new ArrayList<>();
        reportsNotSentYet = new ArrayList<>();

        loadDeviceId();

        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(() -> {
            //to handle when the detail fragment is closed
            detailsFragment = (ReportDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_details);
            if (detailsFragment == null) {
                hideMapElements(View.VISIBLE);
                unHideFloatingButtons();
            }
        });
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        mapFragment = (MapFragment) fm.findFragmentById(R.id.frame_layout_map);
        if (mapFragment==null){
            mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_map,
                    mapFragment).
                    addToBackStack(null).
                    commit();
        }


        mWeatherReportViewModel = new ViewModelProvider(this).get(WeatherReportViewModel.class);
        // Update the cached copy of the reports in the map overlays (method reference style)
        mWeatherReportViewModel.getWeatherReports().observe(this, reports -> setReports(reports, displayNotifs));

        findViewById(R.id.img_btn_settings).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportFormActivity.class);
            startActivityForResult(intent, NEW_REPORT_ACTIVITY_REQUEST_CODE);
        });

        findViewById(R.id.fab_recenter).setOnClickListener(v -> {
            mapFragment.recenterButtonAction();
        });
        //TODO --> switch comment/uncomment after init (clean db)
        //TODO --> comment here
        //mWeatherReportViewModel.clearAllTables();

        //TODO --> uncomment here
        ///*
        service = RetrofitInstance.getInstance().create(WebService.class);
        retrieveReports();

        Handler handler = new Handler();
        int delay = 5*1000; //milliseconds
        //every 60secs here
        //every 10min because --> asynchronous app
        handler.postDelayed(new Runnable() {
            public void run() {
                handler.postDelayed(this, delay);
                retrieveReports();
            }
        }, delay);

        Handler handler2 = new Handler();
        int delay2 = 10*1000; //milliseconds
        handler2.postDelayed(new Runnable() {
            public void run() {
                handler2.postDelayed(this, delay2);
                pushLocalReports();
            }
        }, delay);//*/
    }

    @Override
    public void onResume() {
        super.onResume();
        svm.updateData();
    }

    public void loadDeviceId() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {

            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //IMEI = mngr.getDeviceId();
            deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Toast.makeText(this, "device id: " + deviceId, Toast.LENGTH_SHORT).show();
            // READ_PHONE_STATE permission is already been granted.
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                Toast.makeText(this,"Alredy DONE",Toast.LENGTH_SHORT).show();
                TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                //IMEI = mngr.getDeviceId();
                deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
                Toast.makeText(this, "device id: " + deviceId, Toast.LENGTH_SHORT).show();
                //textView.setText(device_unique_id+"----"+mngr.getDeviceId());

            } else {
                Toast.makeText(this,"too bad...",Toast.LENGTH_SHORT).show();
            }
        }
    }


    void pushLocalReports() {
        /*mWeatherReportViewModel.getWeatherReports().getValue().forEach(r -> {
            if(!r.getReport().getRegistered()) {
                pushOneReport(r, false);
                setReports(mWeatherReportViewModel.getWeatherReports().getValue());//in case
            }
        });*/
        reportsNotSentYet.forEach(r -> {
            pushOneReport(r, false);
            //setReports(mWeatherReportViewModel.getWeatherReports().getValue(), false);//in case
        });
    }

    void pushOneReport(WeatherReport weatherReport, boolean justClicked) {
        Report report = weatherReport.getReport();
        List<RemoteIncident> incidents = new ArrayList<>();
        weatherReport.getMinIncidentList().forEach(i -> {
            incidents.add(new RemoteIncident(ITypeIncident.INCIDENT_MIN, i.getNum()+10, null, null, i.getComment()));
        });
        weatherReport.getBasicIncidentList().forEach(i -> {
            incidents.add(new RemoteIncident(ITypeIncident.INCIDENT_BASIC, i.getNum()+10, i.getLevel(), null, i.getComment()));
        });
        weatherReport.getMeasuredIncidentList().forEach(i -> {
            incidents.add(new RemoteIncident(ITypeIncident.INCIDENT_MEASURED, i.getNum()+10, i.getValue(), i.getUnit(), i.getComment()));
        });
        RemoteWeatherReport toSend = new RemoteWeatherReport(report, incidents);
        Call<RemoteWeatherReport> call = service.postReport(toSend);
        call.enqueue(new Callback<RemoteWeatherReport>() {

            @Override
            public void onResponse(Call<RemoteWeatherReport> call, Response<RemoteWeatherReport> response) {
                if(!justClicked) {
                    int sizeB = reportsNotSentYet.size();
                    reportsNotSentYet.remove(weatherReport);
                    /*Toast.makeText(MainActivity.this, "notSentSize = " + sizeB + " - after: " +
                            reportsNotSentYet.size(), Toast.LENGTH_SHORT).show();*/
                }
                Toast.makeText(MainActivity.this, "Report sent!", Toast.LENGTH_SHORT).show();
                //mWeatherReportViewModel.deleteOldWeatherReport(weatherReport);//rm
                //weatherReport.getReport().setRegistered();//set
                //mWeatherReportViewModel.insert(weatherReport);//reinsert --> registered this time!
                //retrieveReports();
            }

            @Override
            public void onFailure(Call<RemoteWeatherReport> call, Throwable t) {
                if(justClicked) {
                    Toast.makeText(MainActivity.this, "No connection... report will be sent later", Toast.LENGTH_SHORT).show();
                    reportsNotSentYet.add(weatherReport);
                }
                else {
                    List<WeatherReport> tmp = new ArrayList<>(reportsNotSentYet);
                    tmp.addAll(mWeatherReportViewModel.getWeatherReports().getValue());
                    setReports(tmp, false);
                }
            }
        });
    }

    void retrieveReports() {
        Call<List<RemoteWeatherReport>> call = service.getAllReports();
        call.enqueue(new Callback<List<RemoteWeatherReport>>() {
            @Override
            public void onResponse(Call<List<RemoteWeatherReport>> call, Response<List<RemoteWeatherReport>> response) {
                /*if (manualSync)
                    Toast.makeText(MainActivity.this, "Sync...", Toast.LENGTH_SHORT).show();
                */
                //Toast.makeText(MainActivity.this, "Sync...", Toast.LENGTH_SHORT).show();
                List<RemoteWeatherReport> reports = response.body();
                if (reports == null) return;
                List<WeatherReport> toInsertInDb = new ArrayList<>();
                reports.forEach(r -> {
                    Report report = r.getReport();
                    if (report == null || report.getId() == null) return;
                    report.setRegistered();
                    List<MinIncident> minList = new ArrayList<>();
                    List<BasicIncident> basicList = new ArrayList<>();
                    List<MeasuredIncident> measuredList = new ArrayList<>();
                    IncidentFactory_classic factory = new IncidentFactory_classic();
                    r.getIncidents().forEach(i -> {
                        Incident newOne = factory.getIncident(i.getTypeIncident(), i.getTypeInfo(), i.getValue(), i.getUnit(), i.getComment());
                        switch (i.getTypeIncident()) {
                            case ITypeIncident.INCIDENT_MIN:
                                minList.add((MinIncident) newOne);
                                break;
                            case ITypeIncident.INCIDENT_BASIC:
                                basicList.add((BasicIncident) newOne);
                                break;
                            case ITypeIncident.INCIDENT_MEASURED:
                                measuredList.add((MeasuredIncident) newOne);
                                break;
                        }
                    });
                    WeatherReport weatherReport = new WeatherReport(report, minList, basicList, measuredList);
                    weatherReport.getReport().setRegistered();
                    boolean already = alreadyInDB(report.getId());
                    if(!report.getDevice().equals(deviceId) && !already) {
                        reportsNewlyRetrievedFromDB.add(weatherReport);
                    }
                    //if(alreadyInDB(report.getId())) mWeatherReportViewModel.deleteOldWeatherReport(weatherReport);
                    toInsertInDb.add(weatherReport);
                });
                mWeatherReportViewModel.deleteOldWeatherReports(toInsertInDb);//clean db from != reports before injecting
                toInsertInDb.forEach(n -> mWeatherReportViewModel.insert(n));
                //setReports(mWeatherReportViewModel.getWeatherReports().getValue(), false);
                //Toast.makeText(MainActivity.this, "Data retrieved!", Toast.LENGTH_SHORT).show();
                /*((FloatingActionButton) findViewById(R.id.fab_sync)).setImageResource(R.drawable.ic_sync_black_24dp);
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black, null)));
            */
            }

            @Override
            public void onFailure(Call<List<RemoteWeatherReport>> call, Throwable t) {
                /*((FloatingActionButton) findViewById(R.id.fab_sync)).setImageResource(R.drawable.ic_sync_problem_black_24dp);
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red, null)));
                */
                //if (manualSync) Toast.makeText(MainActivity.this, "No connection!...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean alreadyInDB(String id) {
        AtomicBoolean res = new AtomicBoolean(false);
        mWeatherReportViewModel.getWeatherReports().getValue().forEach(r -> {
            //Toast.makeText(this, "foreach", Toast.LENGTH_SHORT).show();
            if(r.getReport().getId().compareTo(id) == 0) {
                //Toast.makeText(this, "alreadyInDb", Toast.LENGTH_SHORT).show();
                res.set(true);
            }
        });
        return res.get();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_REPORT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            double lat = mapFragment.getLastLatitude();
            double lon = mapFragment.getLastLongitude();
            if(lat == -1 || lon == -1) {
                Toast.makeText(
                        getApplicationContext(),
                        "GPS still loading!",
                        Toast.LENGTH_SHORT).show();
                lat = 43.5;
                lon = 6.9;
            }
            Report report = new IncidentFactory_classic().getReport(deviceId, lat, lon);//data.getParcelableExtra(EXTRA_REPORT);
            if(report != null) {
                List<MinIncident> minList = data.getParcelableArrayListExtra(EXTRA_INCIDENT_MIN_LIST);
                List<BasicIncident> basicList= data.getParcelableArrayListExtra(EXTRA_INCIDENT_BASIC_LIST);
                List<MeasuredIncident> measuredList = data.getParcelableArrayListExtra(EXTRA_INCIDENT_MEASURED_LIST);
                if(minList!=null && basicList!=null && measuredList!=null &&
                        (!minList.isEmpty() || !basicList.isEmpty() || !measuredList.isEmpty())) {
                    WeatherReport weatherReport = new WeatherReport(report, minList, basicList, measuredList);
                    //not int db because bug...
                    // --> so if no success then added to tmp list and continuously sent to server until OK
                    //mWeatherReportViewModel.insert(weatherReport);//db first
                    pushOneReport(weatherReport, true);//then server
                }
            }
        }
    }

    public void setReports(List<WeatherReport> reports, boolean displayNotifs) {
        List<OverlayItem> reportItems = new ArrayList<>();
        Map<OverlayItem, WeatherReport> overlayItemWeatherReportMap = new HashMap<>();
        reports.forEach(r -> {
            long time = r.getReport().getTime();
            String txtTime = ""+time;
            OverlayItem item = new OverlayItem("report", "incidents inside fragment",
                    new GeoPoint(r.getReport().getLatitude(), r.getReport().getLongitude()));
            Drawable location_pin;
            if(!r.getReport().getRegistered()) location_pin = getDrawable(R.drawable.ic_location_on_not_registered_36dp);
            else location_pin = getDrawable(R.drawable.ic_location_on_registered_36dp);
            item.setMarker(location_pin);
            reportItems.add(item);
            overlayItemWeatherReportMap.put(item, r);
        });
        reportsNotSentYet.forEach(r -> {
            long time = r.getReport().getTime();
            String txtTime = ""+time;
            OverlayItem item = new OverlayItem("report", "incidents inside fragment",
                    new GeoPoint(r.getReport().getLatitude(), r.getReport().getLongitude()));
            Drawable location_pin = getDrawable(R.drawable.ic_location_on_not_registered_36dp);
            item.setMarker(location_pin);
            reportItems.add(item);
            overlayItemWeatherReportMap.put(item, r);
        });


        int size = reports.size();


        if(size > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reports.get(size - 1).getReport().getTime());
            Date date = new Date(calendar);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



            WeatherReport lastOne = reports.get(size-1);
            Report report = lastOne.getReport();
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this,"chanel1")
                    .setSmallIcon(R.drawable.ic_directions_boat_black_24dp)
                    .setContentTitle("Last report")
                    .setContentText("Report :" +date.getFullHour())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Report :" +date.getFullHour()
                            +"\nlatitude :"+report.getLatitude()
                            +"\nlongitude :"+report.getLongitude()));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (displayNotifs)
                notificationManager.notify(100, builder.build());
        }

        if(displayNotifs) {
            int nb = reportsNewlyRetrievedFromDB.size();
            if(nb!=0) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                String contentTitle = nb + " new report" + (nb==1? "" : "s") + "!";
                String contentText = "since last sync";
                NotificationCompat.Builder builder= new NotificationCompat.Builder(MainActivity.this,"chanel1")
                        .setSmallIcon(R.drawable.ic_directions_boat_black_24dp)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(100, builder.build());
                reportsNewlyRetrievedFromDB.clear();// = new ArrayList<>();//clear
            }
        }

        ReportItemizedOverlay reportOverlayItems = new ReportItemizedOverlay(
                reportItems, getDrawable(R.drawable.direction_arrow), new ReportItemizedOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                hideMapElements(View.GONE);
                //link to fragment
                displayFragment(prepareDetailsFragment(overlayItemWeatherReportMap.get(item)));
                hideFloatingButtons();
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return true;//true??
            }
        }, getApplicationContext());
        mapFragment.updateMap(reportOverlayItems);
    }

    private void hideFloatingButtons(){
        findViewById(R.id.fab_add).setVisibility(View.INVISIBLE);
        findViewById(R.id.fab_recenter).setVisibility(View.INVISIBLE);
        findViewById(R.id.img_btn_settings).setVisibility(View.INVISIBLE);
    }

    private void unHideFloatingButtons(){
        findViewById(R.id.fab_add).setVisibility(View.VISIBLE);
        findViewById(R.id.fab_recenter).setVisibility(View.VISIBLE);
        findViewById(R.id.img_btn_settings).setVisibility(View.VISIBLE);
    }


    private void hideMapElements(int visibility) {
        findViewById(R.id.gpsLocation).setVisibility(visibility);
        findViewById(R.id.img_btn_settings).setVisibility(visibility);
    }

    void displayFragment(Fragment frag) {
        fm
                .beginTransaction()
                .replace(R.id.frame_layout_details, frag)
                .addToBackStack(null)
                .commit();
    }

    private Fragment prepareDetailsFragment(WeatherReport report) {
        ReportDetailsFragment fragment = new ReportDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_REPORT, gson.toJson(report));
        fragment.setArguments(bundle);
        return fragment;
    }

    private void displayCoordinates(boolean value) {
        if (value) {
            findViewById(R.id.gpsLocation).setVisibility(View.VISIBLE);
        } else
            findViewById(R.id.gpsLocation).setVisibility(View.INVISIBLE);
    }


}