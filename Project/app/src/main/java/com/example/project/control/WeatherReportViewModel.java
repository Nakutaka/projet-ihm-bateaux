package com.example.project.control;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project.data.model.WeatherReport;
import com.example.project.main.MainActivity;

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
    public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }
}