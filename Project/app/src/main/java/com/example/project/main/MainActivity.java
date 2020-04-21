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
import com.example.project.data.model.IncidentWithInfo;
import com.example.project.data.model.entities.IncidentDB;
import com.example.project.data.model.entities.Report;
import com.example.project.data.model.entities.ReportWithIncidentsDB;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;
import com.example.project.types.ITypeParam;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPORT = "com.example.project.main.activities.REPORT";
    public static final String EXTRA_INCIDENT_LIST = "com.example.project.main.activities.INCIDENT_LIST";
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


        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportFormActivity.class);
            start = Calendar.getInstance().getTimeInMillis();
            intent.putExtra(ITypeParam.MAP_ACTIVITY_START, start);
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
            Report rTmp = data.getParcelableExtra(EXTRA_REPORT);
            if(rTmp != null) {
                List<IncidentWithInfo> iTmp = data.getParcelableExtra(EXTRA_INCIDENT_LIST);
                if(iTmp != null) {
                    mWeatherReportViewModel.insert(rTmp, iTmp);
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
    public void setReports(List<ReportWithIncidentsDB> reports) {
        List<OverlayItem> reportItems = new ArrayList<>();
        reports.forEach(r -> {
            Report report = r.report;
            List<IncidentDB> incidentDB = new ArrayList<>();
            if(r.cloudIncident!=null) incidentDB.add(r.cloudIncident);
            if(r.currentIncident!=null) incidentDB.add(r.currentIncident);
            if(r.fogIncident!=null) incidentDB.add(r.fogIncident);
            if(r.hailIncident!=null) incidentDB.add(r.hailIncident);
            if(r.otherIncident!=null) incidentDB.add(r.otherIncident);
            if(r.rainIncident!=null) incidentDB.add(r.rainIncident);
            if(r.stormIncident!=null) incidentDB.add(r.stormIncident);
            if(r.temperatureIncident!=null) incidentDB.add(r.temperatureIncident);
            if(r.transparencyIncident!=null) incidentDB.add(r.transparencyIncident);
            if(r.windIncident!=null) incidentDB.add(r.windIncident);
            int hour = (int)((report.reportId/(1000*60*60))%24);
            int min = (int)((report.reportId/(1000*60))%60);
            int sec = (int)(report.reportId/1000)%60;
            StringBuilder sbuf = new StringBuilder();
            Formatter fmt = new Formatter(sbuf);
            String time = fmt.format("%02dh%02dmin%02dsec", hour, min, sec).toString();
            //String time = String.format("%02d:%02d:%02d", hour, min, sec);
            AtomicReference<String> typeCom = new AtomicReference<>("");
            incidentDB.forEach(i -> typeCom.updateAndGet(v -> v + "\n" + i.incidentId + ": " + i.comment));
            reportItems.add(new OverlayItem("Report: " + time + " - " + incidentDB.size()
                    + (incidentDB.size()>1? " incidents" : " incident"), ""+typeCom,
                    new GeoPoint(report.latitude, report.longitude)));
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
        mapFragment.updateMap(reportOverlayItems);
    }
    /////////////////////////////////////////////////////////////////
}