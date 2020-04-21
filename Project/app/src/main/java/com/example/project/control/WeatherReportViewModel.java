package com.example.project.control;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project.R;
import com.example.project.control.WeatherReportRepository;
import com.example.project.data.model.IncidentWithInfo;
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
import com.example.project.data.model.incident.Incident;
import com.example.project.data.model.information.GeneralInformation;
import com.example.project.data.model.information.Information;
import com.example.project.data.model.information.SpecificInformation;

import java.util.List;

public class WeatherReportViewModel extends AndroidViewModel {
    private WeatherReportRepository mRepository;
    private LiveData<List<ReportWithIncidentsDB>> mWeatherReports;
    private LiveData<List<ReportWithIncidentsDB>> mLastWeatherReports;

    public WeatherReportViewModel(Application application) {
        super(application);
        mRepository = new WeatherReportRepository(application);
        mWeatherReports = mRepository.getAllWeatherReports();
        mLastWeatherReports = mRepository.getLastWeatherReports();
    }

    public LiveData<List<ReportWithIncidentsDB>> getWeatherReports() { return mWeatherReports; }
    public LiveData<List<ReportWithIncidentsDB>> getLastWeatherReports() { return mLastWeatherReports; }

    //public void insert(Report report, List<Incident> incidents) { mRepository.insert(report, incidents); }
    public void clearWeatherReports() { mRepository.deleteAllWeatherReports(); }

    public void insert(Report report, List<IncidentWithInfo> in) {
        insert(report);
        long id = report.getReportId();
        double lat = report.getLatitude();
        double lon = report.getLongitude();
        in.forEach(i -> {
            Incident inc = i.getIncident();
            Information info = i.getInfo();
            switch(info.getType()) {
                case R.dimen.info_cloud:
                    insert(new Cloud(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_current:
                    insert(new Current(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_fog:
                    insert(new Fog(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_hail:
                    insert(new Hail(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_other:
                    insert(new Other(id, lat, lon, info.getType(), info.getIcon(), inc.getComment()));
                    break;
                case R.dimen.info_rain:
                    insert(new Rain(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_storm:
                    insert(new Storm(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((GeneralInformation)info).getLevel()));
                    break;
                case R.dimen.info_temperature:
                    insert(new Temperature(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((SpecificInformation)info).getValue(), ((SpecificInformation)info).getUnit()));
                    break;
                case R.dimen.info_transparency:
                    insert(new Transparency(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((SpecificInformation)info).getValue(), ((SpecificInformation)info).getUnit()));
                    break;
                case R.dimen.info_wind:
                    insert(new Wind(id, lat, lon, info.getType(), info.getIcon(), inc.getComment(), ((SpecificInformation)info).getValue(), ((SpecificInformation)info).getUnit()));
                    break;
            }
        });
    }

    private void insert(Report in) { mRepository.insert(in); }
    private void insert(Cloud in) { mRepository.insert(in); }
    private void insert(Current in) { mRepository.insert(in); }
    private void insert(Fog in) { mRepository.insert(in); }
    private void insert(Hail in) { mRepository.insert(in); }
    private void insert(Other in) { mRepository.insert(in); }
    private void insert(Rain in) { mRepository.insert(in); }
    private void insert(Storm in) { mRepository.insert(in); }
    private void insert(Temperature in) { mRepository.insert(in); }
    private void insert(Transparency in) { mRepository.insert(in); }
    private void insert(Wind in) { mRepository.insert(in); }
}