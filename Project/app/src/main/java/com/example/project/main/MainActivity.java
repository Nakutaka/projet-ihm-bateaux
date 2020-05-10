package com.example.project.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.control.WeatherReportViewModel;
import com.example.project.database.remote.RetrofitInstance;
import com.example.project.database.remote.WebService;
import com.example.project.model.unused.Date;
import com.example.project.model.weather.Report;
import com.example.project.model.weather.WeatherReport;
import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.main.forms.ReportFormActivity;
import com.example.project.main.fragments.MapFragment;
import com.example.project.main.fragments.ReportDetailsFragment;
import com.example.project.model.weather.remote.RemoteWeatherReport;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;
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

    private int itemIndex;
    private MapFragment mapFragment;
    private TextView tappedLocation;
    private FrameLayout reportDetails;//TODO
    ReportDetailsFragment detailsFragment;
    private WeatherReportViewModel mWeatherReportViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        itemIndex = -1;
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_map);
        if (mapFragment==null){
            mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_map,
                    mapFragment).commit();
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

        //pushReports();
        //retrieveReports();
    }

    void pushReports() {

    }

    void retrieveReports() {
        WebService service = RetrofitInstance.getInstance().create(WebService.class);
        Call<List<RemoteWeatherReport>> call = service.getAllReports();
        call.enqueue(new Callback<List<RemoteWeatherReport>>() {
            @Override
            public void onResponse(Call<List<RemoteWeatherReport>> call, Response<List<RemoteWeatherReport>> response) {
                //db
            }

            @Override
            public void onFailure(Call<List<RemoteWeatherReport>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No connection...sorry try later!", Toast.LENGTH_SHORT).show();
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

    void EraseReportDetailsFragment() {
        if(detailsFragment!=null) getSupportFragmentManager().beginTransaction().remove(detailsFragment).commit();
    }

    void displayReportDetailsFragment() {
        detailsFragment = new ReportDetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_report_details, detailsFragment)
                .commit();
    }

    //TODO clean (use ReportWithIncidents instead of ReportWithIncidentsDB)
    //TODO + continue (trigger Fragment instead of ugly green box)
    public void setReports(List<WeatherReport> reports) {
        List<OverlayItem> reportItems = new ArrayList<>();
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
            reportItems.add(new OverlayItem("Report: " + txtTime + " - " + r.getMinIncidentList().size() +
                    r.getBasicIncidentList().size() + r.getMeasuredIncidentList().size()
                    + "incident(s)", "" + typeCom,
                    new GeoPoint(r.getReport().getLatitude(), r.getReport().getLongitude())));
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
        ItemizedOverlayWithFocus<OverlayItem> reportOverlayItems = new ItemizedOverlayWithFocus<>(this,
                reportItems, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        EraseReportDetailsFragment();
                        //do something
                        if(index == itemIndex) {
                            //hide Fragment/destroy it
                            itemIndex = -1;
                            tappedLocation.setText("");
                            return false;//change something or not?
                        }
                        WeatherReport current = reports.get(index);
                        Report report = current.getReport();
                        List<MinIncident> minIncidentList = current.getMinIncidentList();
                        List<BasicIncident> basicIncidentList = current.getBasicIncidentList();
                        List<MeasuredIncident> measuredIncidentList = current.getMeasuredIncidentList();
                        //trigger Fragment + fill in fields

                        displayReportDetailsFragment();

                        //pass report + lists to Fragment
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