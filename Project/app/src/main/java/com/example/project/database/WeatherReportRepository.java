package com.example.project.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.project.database.core.WeatherReportDao;
import com.example.project.database.core.WeatherReportRoomDatabase;
import com.example.project.database.models.Cloud;
import com.example.project.database.models.Current;
import com.example.project.database.models.Fog;
import com.example.project.database.models.Hail;
import com.example.project.database.models.Incident;
import com.example.project.database.models.Other;
import com.example.project.database.models.Rain;
import com.example.project.database.models.Report;
import com.example.project.database.models.ReportWithIncidents;
import com.example.project.database.models.Storm;
import com.example.project.database.models.Temperature;
import com.example.project.database.models.Transparency;
import com.example.project.database.models.Wind;
import com.example.project.types.ITypeIncident;

import java.util.List;

public class WeatherReportRepository {

    private WeatherReportDao mWeatherReportDao;

    private LiveData<List<ReportWithIncidents>> mAllReportsWithIncidents;
    private LiveData<List<ReportWithIncidents>> mLastReportsWithIncidents;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    WeatherReportRepository(Application application) {
        WeatherReportRoomDatabase db = WeatherReportRoomDatabase.getDatabase(application);
        mWeatherReportDao = db.weatherReportDao();
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

    void insert(Report report) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(report);
        });
    }

    void insert(Cloud cloud) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(cloud);
        });
    }

    void insert(Current in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }
    void insert(Fog in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Hail in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Other in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Rain in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Storm in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Temperature in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Transparency in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void insert(Wind in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
        });
    }

    void deleteAllWeatherReports() {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.deleteAllIncidentsCloud();
            mWeatherReportDao.deleteAllIncidentsCurrent();
            mWeatherReportDao.deleteAllIncidentsFog();
            mWeatherReportDao.deleteAllIncidentsHail();
            mWeatherReportDao.deleteAllIncidentsOther();
            mWeatherReportDao.deleteAllIncidentsRain();
            mWeatherReportDao.deleteAllIncidentsStorm();
            mWeatherReportDao.deleteAllIncidentsTemperature();
            mWeatherReportDao.deleteAllIncidentsTransparency();
            mWeatherReportDao.deleteAllIncidentsWind();
            mWeatherReportDao.deleteAllReports();
        });
        WeatherReportRoomDatabase.populate();//repopulate
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Report report, List<Incident> incidents) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(report);
            //incidents.forEach(this::insert);//lambda
        });
    }

    private void insert(Incident incident) {
        if(incident==null) return;
        switch(incident.incidentId) {
            case (ITypeIncident.INCIDENT_CLOUD): {
                Cloud tmp = new Cloud((Cloud)incident);
                mWeatherReportDao.insert(tmp);
            } break;
            case (ITypeIncident.INCIDENT_CURRENT): mWeatherReportDao.insert((Current)incident); break;
            case (ITypeIncident.INCIDENT_FOG): mWeatherReportDao.insert((Fog)incident); break;
            case (ITypeIncident.INCIDENT_HAIL): mWeatherReportDao.insert((Hail)incident); break;
            case (ITypeIncident.INCIDENT_RAIN): mWeatherReportDao.insert((Rain)incident); break;
            case (ITypeIncident.INCIDENT_STORM): mWeatherReportDao.insert((Storm)incident); break;
            case (ITypeIncident.INCIDENT_TEMPERATURE): mWeatherReportDao.insert((Temperature)incident); break;
            case (ITypeIncident.INCIDENT_TRANSPARENCY): mWeatherReportDao.insert((Transparency)incident); break;
            case (ITypeIncident.INCIDENT_WIND): mWeatherReportDao.insert((Wind)incident); break;
            default: mWeatherReportDao.insert((Other)incident);
        }
    }
}