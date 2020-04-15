package com.example.projet.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projet.database.models.Incident;
import com.example.projet.database.models.ReportWithIncidents;

import java.util.List;

class WeatherReportRepository {

    private WeatherReportDao mWeatherReportDao;

    private LiveData<List<ReportWithIncidents>> mAllReportsWithIncidents;
    private LiveData<List<ReportWithIncidents>> mLastReportsWithIncidents;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    WeatherReportRepository(Application application) {
        WeatherReportRoomDatabase db = WeatherReportRoomDatabase.getDatabase(application);
        mWeatherReportDao = db.wordDao();
        mAllReportsWithIncidents = mWeatherReportDao.getReportsWithIncidents();
        mLastReportsWithIncidents = mWeatherReportDao.getLastReportsWithIncidents();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<ReportWithIncidents>> getAllWeatherReports() {
        return mAllReportsWithIncidents;
    }
    LiveData<List<ReportWithIncidents>> getLastWeatherReports() {
        return mLastReportsWithIncidents;
    }


    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(ReportWithIncidents reportWithIncidents) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            //long id =  mWeatherReportDao.insert(reportWithIncidents.report);
            mWeatherReportDao.insert(reportWithIncidents.report);
            //reportWithIncidents.incidents.forEach(incident -> insert(incident));//lambda
            //reportWithIncidents.setReportIdToIncident(id);
            reportWithIncidents.incidents.forEach(mWeatherReportDao::insert);//(this::insert);//method reference
        });
    }

    private void insert(Incident incident) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(incident);
        });
    }

    void deleteAllWeatherReports() {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.deleteAllIncidents();
            mWeatherReportDao.deleteAllReports();
        });
        WeatherReportRoomDatabase.populate();//repopulate
    }
}