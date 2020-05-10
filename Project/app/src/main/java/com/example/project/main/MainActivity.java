package com.example.project.main;

import android.app.PendingIntent;
import android.content.Intent;
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
import com.example.project.data.model.Date;
import com.example.project.data.model.Report;
import com.example.project.data.model.WeatherReport;
import com.example.project.data.model.incident.BasicIncident;
import com.example.project.data.model.incident.MeasuredIncident;
import com.example.project.data.model.incident.MinIncident;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;
import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPORT = "com.example.project.main.activities.REPORT";
    //public static final String EXTRA_INCIDENT_LIST = "com.example.project.main.activities.INCIDENT_LIST";
    public static final String EXTRA_INCIDENT_MIN_LIST = "com.example.project.main.activities.INCIDENT_MIN_LIST";
    public static final String EXTRA_INCIDENT_BASIC_LIST = "com.example.project.main.activities.INCIDENT_BASIC_LIST";
    public static final String EXTRA_INCIDENT_MEASURED_LIST = "com.example.project.main.activities.INCIDENT_MEASURED_LIST";
    public long start;
    private Gson gson = new Gson();
    private int itemIndex;
    private MapFragment mapFragment;
    private TextView tappedLocation;
    private FrameLayout reportDetails;//TODO
    private WeatherReportViewModel mWeatherReportViewModel;
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

        findViewById(R.id.fab_erase).setOnClickListener(v -> {
            mWeatherReportViewModel.clearWeatherReports();
        });

        findViewById(R.id.fab_recenter).setOnClickListener(v -> {
            mapFragment.recenterButtonAction();
        });

        tappedLocation = findViewById(R.id.tapped_location);
        reportDetails = findViewById(R.id.frame_layout_report_details);
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
                    mWeatherReportViewModel.insert(weatherReport);
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
            Date d = r.getReport().getDate();
            String time = d.getFullHour();
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

        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        int x = reports.size();
        Date d = reports.get(x-1).getReport().getDate();
        String t = d.getFullHour();

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,"chanel1")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("New notification")
                .setContentText("Report :"+t)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);




        notificationManager.notify(100,builder.build());

        //link to trigger Fragment ReportDetailsFragment
        ReportItemizedOverlay reportOverlayItems = new ReportItemizedOverlay(
                reportItems, getDrawable(R.drawable.direction_arrow), new ReportItemizedOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        hideMapElements(View.GONE);
                        displayFragment(prepareDetailsFragment(overlayItemWeatherReportMap.get(item)));
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


    private void hideMapElements(int visibility) {
        findViewById(R.id.gpsLocation).setVisibility(visibility);
        findViewById(R.id.tapped_location).setVisibility(visibility);
        findViewById(R.id.fab_erase).setVisibility(visibility);
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