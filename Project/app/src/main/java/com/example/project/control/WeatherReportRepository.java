package com.example.project.control;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.project.data.localdb.WeatherReportDao;
import com.example.project.data.localdb.WeatherReportRoomDatabase;
import com.example.project.data.model.Report;
import com.example.project.data.model.WeatherReport;
import com.example.project.data.model.incident.BasicIncident;

import java.util.List;

class WeatherReportRepository {

    private WeatherReportDao mWeatherReportDao;
    private LiveData<List<WeatherReport>> mAllReports;
    private LiveData<List<WeatherReport>> mLastReports;

    private Application app;

    WeatherReportRepository(Application application) {
        app = application;
        WeatherReportRoomDatabase db = WeatherReportRoomDatabase.getDatabase(application);
        mWeatherReportDao = db.weatherReportDao();
        mAllReports = mWeatherReportDao.getAllReportsWithIncidents();
        mLastReports = mWeatherReportDao.getTenLastReportsWithIncidents();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::
    // Room executes all queries on a separate thread
    // Observed LiveData will notify the observer when the data has changed
    LiveData<List<WeatherReport>> getAllWeatherReports() {
        return mAllReports;
    }

    LiveData<List<WeatherReport>> getLastWeatherReports() {
        return mLastReports;
    }

    void insert(WeatherReport report) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(report.getReport());
            String id = report.getReport().getId();
            report.getMinIncidentList().forEach(i -> i.setReportId(id));
            report.getMinIncidentList().forEach(mWeatherReportDao::insert);
            report.getBasicIncidentList().forEach(i -> i.setReportId(id));
            report.getBasicIncidentList().forEach(mWeatherReportDao::insert);
            report.getMeasuredIncidentList().forEach(i -> i.setReportId(id));
            report.getMeasuredIncidentList().forEach(mWeatherReportDao::insert);
        });
    }


    void deleteAllWeatherReports() {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.deleteAllMinIncidents();
            mWeatherReportDao.deleteAllBasicIncidents();
            mWeatherReportDao.deleteAllMeasuredIncidents();
            mWeatherReportDao.deleteAllReports();
        });
        WeatherReportRoomDatabase.populate();//repopulate
    }
}