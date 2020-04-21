package com.example.project.control;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.project.data.localdb.WeatherReportDao;
import com.example.project.data.localdb.WeatherReportRoomDatabase;
import com.example.project.data.model.entities.Cloud;
import com.example.project.data.model.entities.Current;
import com.example.project.data.model.entities.Fog;
import com.example.project.data.model.entities.Hail;
import com.example.project.data.model.entities.Other;
import com.example.project.data.model.entities.Rain;
import com.example.project.data.model.entities.Report;
import com.example.project.data.model.entities.ReportWithIncidentsDB;
import com.example.project.data.model.entities.Storm;
import com.example.project.data.model.entities.Temperature;
import com.example.project.data.model.entities.Transparency;
import com.example.project.data.model.entities.Wind;

import java.util.List;

class WeatherReportRepository {

    private WeatherReportDao mWeatherReportDao;
    private LiveData<List<ReportWithIncidentsDB>> mAllReportsWithIncidents;
    private LiveData<List<ReportWithIncidentsDB>> mLastReportsWithIncidents;

    WeatherReportRepository(Application application) {
        WeatherReportRoomDatabase db = WeatherReportRoomDatabase.getDatabase(application);
        mWeatherReportDao = db.weatherReportDao();
        mAllReportsWithIncidents = mWeatherReportDao.getReportsWithIncidents();
        mLastReportsWithIncidents = mWeatherReportDao.getLastReportsWithIncidents();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::
    // Room executes all queries on a separate thread
    // Observed LiveData will notify the observer when the data has changed
    LiveData<List<ReportWithIncidentsDB>> getAllWeatherReports() {
        return mAllReportsWithIncidents;
    }

    LiveData<List<ReportWithIncidentsDB>> getLastWeatherReports() {
        return mLastReportsWithIncidents;
    }

    void insert(Report report) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(report);
        });
    }

    void insert(Cloud in) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(in);
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
    /*void insert(Report report, List<IncidentDB> incidentDBS) {
        WeatherReportRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherReportDao.insert(report);
            //incidents.forEach(this::insert);//lambda
        });
    }*/

    /*private void insert(IncidentDB incidentDB) {
        if(incidentDB ==null) return;
        switch(incidentDB.incidentId) {
            case (ITypeIncident.CLOUD): {
                Cloud tmp = new Cloud((Cloud) incidentDB);
                mWeatherReportDao.insert(tmp);
            } break;
            case (ITypeIncident.CURRENT): mWeatherReportDao.insert((Current) incidentDB); break;
            case (ITypeIncident.FOG): mWeatherReportDao.insert((Fog) incidentDB); break;
            case (ITypeIncident.HAIL): mWeatherReportDao.insert((Hail) incidentDB); break;
            case (ITypeIncident.RAIN): mWeatherReportDao.insert((Rain) incidentDB); break;
            case (ITypeIncident.STORM): mWeatherReportDao.insert((Storm) incidentDB); break;
            case (ITypeIncident.TEMPERATURE): mWeatherReportDao.insert((Temperature) incidentDB); break;
            case (ITypeIncident.TRANSPARENCY): mWeatherReportDao.insert((Transparency) incidentDB); break;
            case (ITypeIncident.WIND): mWeatherReportDao.insert((Wind) incidentDB); break;
            default: mWeatherReportDao.insert((Other) incidentDB);
        }
    }*/
}