package com.example.project.control;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project.model.weather.WeatherReport;

import java.util.ArrayList;
import java.util.List;

public class WeatherReportViewModel extends AndroidViewModel {
    private WeatherReportRepository mRepository;
    private LiveData<List<WeatherReport>> mWeatherReports;
    private LiveData<List<WeatherReport>> mLastWeatherReports;

    public WeatherReportViewModel(Application application) {
        super(application);
        mRepository = new WeatherReportRepository(application);
        mWeatherReports = mRepository.getAllWeatherReports();
        mLastWeatherReports = mRepository.getLastWeatherReports();
    }

    public LiveData<List<WeatherReport>> getWeatherReports() { return mWeatherReports; }
    public LiveData<List<WeatherReport>> getLastWeatherReports() { return mLastWeatherReports; }
    public void insert(WeatherReport weatherReport) { mRepository.insert(weatherReport); }
    //public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }
    public void clearAllTables() { mRepository.clearAllTables(); }
    private void deleteWeatherReports(List<WeatherReport> reports) { mRepository.deleteWeatherReports(reports); }

    public void deleteOldWeatherReports(List<WeatherReport> toInsertInDb) {
        List<WeatherReport> toDelete = new ArrayList<>();
        for (WeatherReport weatherReport : getWeatherReports().getValue()) {
            boolean toPutInToDelete = true;
            for (WeatherReport toInsert : toInsertInDb) {
                if(weatherReport.getReport().getId().compareTo(toInsert.getReport().getId()) == 0) {
                    toPutInToDelete = false;
                    break;
                }
            }
            if(toPutInToDelete) toDelete.add(weatherReport);
        }
        deleteWeatherReports(toDelete);
    }

    public void deleteOldWeatherReport(WeatherReport toInsert) {
        List<WeatherReport> toDelete = new ArrayList<>();
        for (WeatherReport weatherReport : getWeatherReports().getValue()) {
            boolean toPutInToDelete = true;
            if(weatherReport.getReport().getId().compareTo(toInsert.getReport().getId()) == 0) {
                toPutInToDelete = false;
            }
            if(toPutInToDelete) {
                toDelete.add(weatherReport);
                break;
            }
        }
        deleteWeatherReports(toDelete);
    }
}