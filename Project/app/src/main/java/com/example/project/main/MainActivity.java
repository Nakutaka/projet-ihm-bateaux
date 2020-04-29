package com.example.project.main;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.control.WeatherReportViewModel;
import com.example.project.data.model.incident.BasicIncident;
import com.example.project.data.model.Date;
import com.example.project.data.model.incident.MeasuredIncident;
import com.example.project.data.model.incident.MinIncident;
import com.example.project.data.model.Report;
import com.example.project.data.model.WeatherReport;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPORT = "com.example.project.main.activities.REPORT";
    //public static final String EXTRA_INCIDENT_LIST = "com.example.project.main.activities.INCIDENT_LIST";
    public static final String EXTRA_INCIDENT_MIN_LIST = "com.example.project.main.activities.INCIDENT_MIN_LIST";
    public static final String EXTRA_INCIDENT_BASIC_LIST = "com.example.project.main.activities.INCIDENT_BASIC_LIST";
    public static final String EXTRA_INCIDENT_MEASURED_LIST = "com.example.project.main.activities.INCIDENT_MEASURED_LIST";
    public long start;

    private int itemIndex;
    private MapFragment mapFragment;
    private TextView tappedLocation;
    private FrameLayout reportDetails;//TODO
    private WeatherReportViewModel mWeatherReportViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        itemIndex = -1;
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_map,
                mapFragment).commit();

        mWeatherReportViewModel = new ViewModelProvider(this).get(WeatherReportViewModel.class);
        // Update the cached copy of the reports in the map overlays (method reference style)
        mWeatherReportViewModel.getWeatherReports().observe(this, this::setReports);
/*
        findViewById(R.id.settings).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
*/
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

    void displayReportDetailsFragment() {
        ReportDetailsFragment details = new ReportDetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_report_details, details)
                .commit();
    }

    //TODO clean (use ReportWithIncidents instead of ReportWithIncidentsDB)
    //TODO + continue (trigger Fragment instead of ugly green box)
    public void setReports(List<WeatherReport> reports) {
        List<OverlayItem> reportItems = new ArrayList<>();
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
            reportItems.add(new OverlayItem("Report: " + time + " - " + r.getMinIncidentList().size() +
                    r.getBasicIncidentList().size() + r.getMeasuredIncidentList().size()
                    + "incident(s)", "" + typeCom,
                    new GeoPoint(r.getReport().getLatitude(), r.getReport().getLongitude())));
        });

        //link to trigger Fragment ReportDetailsFragment
        ItemizedOverlayWithFocus<OverlayItem> reportOverlayItems = new ItemizedOverlayWithFocus<>(this,
                reportItems, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        if(index == itemIndex) {
                            //hide Fragment/destroy it
                            itemIndex = -1;
                            tappedLocation.setText("");
                            return false;//change something or not?
                        }
                        //trigger Fragment + fill in fields
                        itemIndex = index;
                        tappedLocation.setText(item.getPoint().getLatitude() + " - " + item.getPoint().getLongitude());
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;//true??
                    }
                });
        //ugly green box
        reportOverlayItems.setFocusItemsOnTap(true);
        mapFragment.updateMap(reportOverlayItems);
    }
}