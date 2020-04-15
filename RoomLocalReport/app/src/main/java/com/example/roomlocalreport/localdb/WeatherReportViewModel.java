package com.example.roomlocalreport.localdb;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomlocalreport.localdb.models.Report;
import com.example.roomlocalreport.localdb.models.ReportWithIncidents;
import java.util.List;

public class WeatherReportViewModel extends AndroidViewModel {
    private WeatherReportRepository mRepository;

    private LiveData<List<ReportWithIncidents>> mWeatherReports;

    public WeatherReportViewModel(Application application) {
        super(application);
        mRepository = new WeatherReportRepository(application);
        mWeatherReports = mRepository.getLastWeatherReports();
    }

    public LiveData<List<ReportWithIncidents>> getLastWeatherReports() { return mWeatherReports; }

    public void insert(ReportWithIncidents weatherReports) { mRepository.insert(weatherReports); }

    public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }
}