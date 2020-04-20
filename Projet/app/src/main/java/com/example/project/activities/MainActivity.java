package com.example.projet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet.R;
import com.example.projet.activities.forms.ReportFormActivity;
import com.example.projet.database.WeatherReportViewModel;
import com.example.projet.database.models.Cloud;
import com.example.projet.database.models.Current;
import com.example.projet.database.models.Fog;
import com.example.projet.database.models.Hail;
import com.example.projet.database.models.Incident;
import com.example.projet.database.models.Other;
import com.example.projet.database.models.Rain;
import com.example.projet.database.models.Report;
import com.example.projet.database.models.ReportWithIncidents;
import com.example.projet.database.models.Storm;
import com.example.projet.database.models.Temperature;
import com.example.projet.database.models.Transparency;
import com.example.projet.database.models.Wind;
import com.example.projet.fragments.MapFragment;
import com.example.projet.fragments.ReportDetailsFragment;
import com.example.projet.types.ITypeIncident;
import com.example.projet.types.ITypeParam;

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
    //public static final String EXTRA_INCIDENT_LIST = "com.example.projet.activities.INCIDENT_LIST";
    public static final String EXTRA_INCIDENT_CLOUD = "com.example.projet.activities.INCIDENT_CLOUD";
    public static final String EXTRA_INCIDENT_CURRENT = "com.example.projet.activities.INCIDENT_CURRENT";
    public static final String EXTRA_INCIDENT_FOG = "com.example.projet.activities.INCIDENT_FOG";
    public static final String EXTRA_INCIDENT_HAIL = "com.example.projet.activities.INCIDENT_HAIL";
    public static final String EXTRA_INCIDENT_OTHER = "com.example.projet.activities.INCIDENT_OTHER";
    public static final String EXTRA_INCIDENT_RAIN = "com.example.projet.activities.INCIDENT_RAIN";
    public static final String EXTRA_INCIDENT_STORM = "com.example.projet.activities.INCIDENT_STORM";
    public static final String EXTRA_INCIDENT_TEMPERATURE = "com.example.projet.activities.INCIDENT_TEMP";
    public static final String EXTRA_INCIDENT_TRANSPARENCY = "com.example.projet.activities.INCIDENT_TRANSPARENCY";
    public static final String EXTRA_INCIDENT_WIND = "com.example.projet.activities.INCIDENT_WIND";
    public long start;

    private int itemIndex;
    private MapFragment mapFragment;
    private TextView tappedLocation;
    private FrameLayout reportDetails;
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

/**********retenter avec list au cas où marcherait sinon un par un marche aussi*****************/
            //List<Incident> incidents = data.getParcelableArrayListExtra(EXTRA_INCIDENT_LIST);
            //List<Incident> incidents = new ArrayList<>();
            Report report = new Report(start, mapFragment.getCurrentLatitude(),
                    mapFragment.getCurrentLongitude());
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
        }
        //setReports triggered automatically (observe)*/
    }

    /**************************To move into ReportRepository + update ReportViewModel
     * only list<Incident> + Report transferred --> cleaner/shorter ****************************************/
    private void insertIncident(Incident newInc){
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

    public void setReports(List<ReportWithIncidents> reports) {
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