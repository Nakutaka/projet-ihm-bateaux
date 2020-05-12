package com.example.project.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.control.WeatherReportViewModel;
import com.example.project.database.remote.RetrofitInstance;
import com.example.project.database.remote.WebService;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;
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

    private MapFragment mapFragment;
    ReportDetailsFragment detailsFragment;
    private WeatherReportViewModel mWeatherReportViewModel;
    private WebService service;
    private FragmentManager fm;
    private List<WeatherReport> reportsNewlyRetrievedFromDB;
    private List<WeatherReport> reportsNotSentYet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reportsNewlyRetrievedFromDB = new ArrayList<>();
        reportsNotSentYet = new ArrayList<>();
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
        mWeatherReportViewModel.getWeatherReports().observe(this, this::setReports);

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
        int delay = 2*1000; //milliseconds
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

    void pushLocalReports() {
        /*mWeatherReportViewModel.getWeatherReports().getValue().forEach(r -> {
            if(!r.getReport().getRegistered()) {
                pushOneReport(r, false);
                setReports(mWeatherReportViewModel.getWeatherReports().getValue());//in case
            }
        });*/
        reportsNotSentYet.forEach(r -> {
            pushOneReport(r, false);
            setReports(mWeatherReportViewModel.getWeatherReports().getValue());//in case
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
                if(justClicked) Toast.makeText(MainActivity.this, "No connection... report will be sent later", Toast.LENGTH_SHORT).show();
                reportsNotSentYet.add(weatherReport);
                List<WeatherReport> tmp = new ArrayList<>(reportsNotSentYet);
                tmp.addAll(mWeatherReportViewModel.getWeatherReports().getValue());
                setReports(tmp);
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
                    if(!report.getDevice().equals("my-device") && !already) {
                        reportsNewlyRetrievedFromDB.add(weatherReport);
                    }
                    //if(alreadyInDB(report.getId())) mWeatherReportViewModel.deleteOldWeatherReport(weatherReport);
                    toInsertInDb.add(weatherReport);
                });
                mWeatherReportViewModel.deleteOldWeatherReports(toInsertInDb);//clean db from != reports before injecting
                toInsertInDb.forEach(n -> mWeatherReportViewModel.insert(n));
                setReports(mWeatherReportViewModel.getWeatherReports().getValue());
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
            Report report = new IncidentFactory_classic().getReport(lat, lon);//data.getParcelableExtra(EXTRA_REPORT);
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

    public void setReports(List<WeatherReport> reports) {
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

}