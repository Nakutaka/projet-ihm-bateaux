package com.example.project.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPORT = "com.example.project.main.activities.REPORT";
    public static final String EXTRA_INCIDENT_MIN_LIST = "com.example.project.main.activities.INCIDENT_MIN_LIST";
    public static final String EXTRA_INCIDENT_BASIC_LIST = "com.example.project.main.activities.INCIDENT_BASIC_LIST";
    public static final String EXTRA_INCIDENT_MEASURED_LIST = "com.example.project.main.activities.INCIDENT_MEASURED_LIST";
    public long start;
    private Gson gson = new Gson();

    private int itemIndex;
    private MapFragment mapFragment;
    private TextView tappedLocation;
    private FrameLayout reportDetails;//TODO
    ReportDetailsFragment detailsFragment;
    private WeatherReportViewModel mWeatherReportViewModel;
    private WebService service;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //to handle when the detail fragment is closed
                Fragment reportDetails = getSupportFragmentManager().findFragmentById(R.id.frame_layout_details);
                if (reportDetails == null) {
                    hideMapElements(View.VISIBLE);
                    unHideFloatingButtons();
                }
            }
        });
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        itemIndex = -1;
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

        /*findViewById(R.id.fab_erase).setOnClickListener(v -> {
            mWeatherReportViewModel.clearWeatherReports();
        });*/

        findViewById(R.id.fab_recenter).setOnClickListener(v -> {
            mapFragment.recenterButtonAction();
        });

        tappedLocation = findViewById(R.id.tapped_location);
        reportDetails = findViewById(R.id.frame_layout_report_details);

        service = RetrofitInstance.getInstance().create(WebService.class);

        retrieveReports(false);

        findViewById(R.id.fab_sync).setOnClickListener((v -> {
            retrieveReports(true);
        }));
    }

    void pushOneReport(WeatherReport weatherReport) {
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
                Toast.makeText(MainActivity.this, "Report sent!", Toast.LENGTH_SHORT).show();
                retrieveReports(false);//not pretty but for now voila...
            }

            @Override
            public void onFailure(Call<RemoteWeatherReport> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Report not sent...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void retrieveReports(boolean manualSync) {
        Call<List<RemoteWeatherReport>> call = service.getAllReports();
        call.enqueue(new Callback<List<RemoteWeatherReport>>() {
            @Override
            public void onResponse(Call<List<RemoteWeatherReport>> call, Response<List<RemoteWeatherReport>> response) {
                //db
                if (manualSync)
                    Toast.makeText(MainActivity.this, "Sync...", Toast.LENGTH_SHORT).show();
                List<RemoteWeatherReport> reports = response.body();
                if (reports == null) return;
                reports.forEach(r -> {
                    Report report = r.getReport();
                    if (report == null) return;
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
                    mWeatherReportViewModel.insert(weatherReport);
                });
                //Toast.makeText(MainActivity.this, "Data retrieved!", Toast.LENGTH_SHORT).show();
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageResource(R.drawable.ic_sync_black_24dp);
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black, null)));
            }

            @Override
            public void onFailure(Call<List<RemoteWeatherReport>> call, Throwable t) {
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageResource(R.drawable.ic_sync_problem_black_24dp);
                ((FloatingActionButton) findViewById(R.id.fab_sync)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red, null)));
                if (manualSync) Toast.makeText(MainActivity.this, "No connection!...", Toast.LENGTH_SHORT).show();
            }
        });
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
                    pushOneReport(weatherReport);
                    //then --> linked with db, but for now only one or the other
                    //mWeatherReportViewModel.insert(weatherReport);
                    Toast.makeText(
                            getApplicationContext(),
                            "Report added!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(
                getApplicationContext(),
                "No report",
                Toast.LENGTH_SHORT).show();
        //setReports triggered automatically (observe)
    }

    //TODO clean (use ReportWithIncidents instead of ReportWithIncidentsDB)
    //TODO + continue (trigger Fragment instead of ugly green box)
    public void setReports(List<WeatherReport> reports) {
        List<OverlayItem> reportItems = new ArrayList<>();
        Map<OverlayItem, WeatherReport> overlayItemWeatherReportMap = new HashMap<>();
        reports.forEach(r -> {
            //Date d = r.getReport().getDate();
            long time = r.getReport().getTime();
            //String time = d.getFullHour();
            String txtTime = ""+time;
            AtomicReference<String> typeCom = new AtomicReference<>("");
            r.getMinIncidentList().forEach(i -> typeCom.updateAndGet(v -> v + "\n" + i.getNum()
                    + ": " + i.getInfo().getName() + " - " + i.getInfo().getIcon()
                    + " - " + i.getComment()));
            r.getBasicIncidentList().forEach(i -> typeCom.updateAndGet(v -> v + "\n" + i.getNum()
                    + ": " + i.getInfo().getName() + " - " + i.getInfo().getIcon()
                    + " - " + i.getLevel() + " - " + i.getComment()));
            r.getMeasuredIncidentList().forEach(i -> typeCom.updateAndGet(v -> v + "\n" + i.getNum()
                    + ": " + i.getInfo().getName() + " - " + i.getInfo().getIcon()
                    + " - " + i.getValue() + i.getUnit() + " - " + i.getComment()));
            OverlayItem item = new OverlayItem("Report: " + time + " - " + r.getMinIncidentList().size() +
                    r.getBasicIncidentList().size() + r.getMeasuredIncidentList().size()
                    + "incident(s)", "" + typeCom,
                    new GeoPoint(r.getReport().getLatitude(), r.getReport().getLongitude()));
            Drawable drawable = getDrawable(R.drawable.ic_place_black_36dp);
            item.setMarker(drawable);
            reportItems.add(item);
            overlayItemWeatherReportMap.put(item, r);
        });

        int size = reports.size();
        if(size > 0) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            WeatherReport lastOne = reports.get(size-1);
            Report report = lastOne.getReport();
            String txtTime = "" + report.getTime();
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this,"chanel1")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle("New notification")
                    .setContentText("Report :" + txtTime)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(100, builder.build());
        }

        //link to trigger Fragment ReportDetailsFragment
        ReportItemizedOverlay reportOverlayItems = new ReportItemizedOverlay(
                reportItems, getDrawable(R.drawable.direction_arrow), new ReportItemizedOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        hideMapElements(View.GONE);
                        displayFragment(prepareDetailsFragment(overlayItemWeatherReportMap.get(item)));
                        hideFloatingButtons();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;//true??
                    }
        }, getApplicationContext());
        //ugly green box -- not anymore
        reportOverlayItems.setFocusItemsOnTap(true);
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
        findViewById(R.id.tapped_location).setVisibility(visibility);
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