package com.example.roomlocalreport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.roomlocalreport.R;
import com.example.roomlocalreport.localdb.WeatherReportListAdapter;
import com.example.roomlocalreport.localdb.WeatherReportViewModel;
import com.example.roomlocalreport.localdb.models.Report;
import com.example.roomlocalreport.localdb.models.ReportWithIncidents;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private WeatherReportViewModel mWeatherReportViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WeatherReportListAdapter adapter = new WeatherReportListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWeatherReportViewModel = new ViewModelProvider(this).get(WeatherReportViewModel.class);

        // Update the cached copy of the reports in the adapter. (method reference style)
        mWeatherReportViewModel.getLastWeatherReports().observe(this, adapter::setReports);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton fabErase = findViewById(R.id.fab_erase);
        fabErase.setOnClickListener(view -> {
            mWeatherReportViewModel.clearWeatherReports();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Date currentDate = Calendar.getInstance().getTime();
            long time = currentDate.getTime();
            Report report = new Report(time);
            ReportWithIncidents weatherReport = new ReportWithIncidents(report);
            weatherReport.addIncident(time,0, data.getStringExtra(ReportActivity.EXTRA_REPLY));
            mWeatherReportViewModel.insert(weatherReport);
            //after other code --> other button to add incidents/comments
            Toast.makeText(
                    getApplicationContext(),
                    R.string.saved,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
