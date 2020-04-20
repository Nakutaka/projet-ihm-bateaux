package com.example.project.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project.database.models.Cloud;
import com.example.project.database.models.Current;
import com.example.project.database.models.Fog;
import com.example.project.database.models.Hail;
import com.example.project.database.models.Other;
import com.example.project.database.models.Rain;
import com.example.project.database.models.Report;
import com.example.project.database.models.ReportWithIncidents;
import com.example.project.database.models.Storm;
import com.example.project.database.models.Temperature;
import com.example.project.database.models.Transparency;
import com.example.project.database.models.Wind;

import java.util.List;

public class WeatherReportViewModel extends AndroidViewModel {
    private WeatherReportRepository mRepository;
    private LiveData<List<ReportWithIncidents>> mWeatherReports;
    private LiveData<List<ReportWithIncidents>> mLastWeatherReports;

    public WeatherReportViewModel(Application application) {
        super(application);
        mRepository = new WeatherReportRepository(application);
        mWeatherReports = mRepository.getAllWeatherReports();
        mLastWeatherReports = mRepository.getLastWeatherReports();
    }

    public LiveData<List<ReportWithIncidents>> getWeatherReports() { return mWeatherReports; }
    public LiveData<List<ReportWithIncidents>> getLastWeatherReports() { return mLastWeatherReports; }

    //public void insert(Report report, List<Incident> incidents) { mRepository.insert(report, incidents); }
    public void insert(Report report) { mRepository.insert(report); }
    public void insert(Cloud in) { mRepository.insert(in); }
    public void insert(Current in) { mRepository.insert(in); }
    public void insert(Fog in) { mRepository.insert(in); }
    public void insert(Hail in) { mRepository.insert(in); }
    public void insert(Other in) { mRepository.insert(in); }
    public void insert(Rain in) { mRepository.insert(in); }
    public void insert(Storm in) { mRepository.insert(in); }
    public void insert(Temperature in) { mRepository.insert(in); }
    public void insert(Transparency in) { mRepository.insert(in); }
    public void insert(Wind in) { mRepository.insert(in); }

    public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }
}