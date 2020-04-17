package com.example.projet.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projet.database.models.Cloud;
import com.example.projet.database.models.Incident;
import com.example.projet.database.models.Report;
import com.example.projet.database.models.ReportWithIncidents;

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

    public LiveData<List<ReportWithIncidents>> getWeatherReports() {

        return mWeatherReports;
    }
    public LiveData<List<ReportWithIncidents>> getLastWeatherReports() { return mLastWeatherReports; }
    public void insert(Report report, List<Incident> incidents) { mRepository.insert(report, incidents); }
    public void insert(Cloud cloud) { mRepository.insert(cloud); }
    public void insert(Report report) { mRepository.insert(report); }
    public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }
}