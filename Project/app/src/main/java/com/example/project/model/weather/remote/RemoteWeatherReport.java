package com.example.project.data.model.weather.remote;

import com.example.project.data.model.weather.Report;

import java.util.List;

public class RemoteWeatherReport {

    public Report report;
    public List<RemoteIncident> incidents;

    //new IncidentFactory_classic().getIncident(int typeIncident, int typeInformation, String value, String unit, String comment);
    //public abstract Incident getIncident(int typeIncident, int typeInformation, String value, String unit, String comment);

    public RemoteWeatherReport(Report report, List<RemoteIncident> incidents) {
        this.report = report;
        this.incidents = incidents;
    }

    public Report getReport() {
        return report;
    }

    public List<RemoteIncident> getIncidents() {
        return incidents;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setIncidents(List<RemoteIncident> incidents) {
        this.incidents = incidents;
    }
}
