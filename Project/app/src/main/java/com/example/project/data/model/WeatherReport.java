package com.example.project.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.project.data.model.incident.BasicIncident;
import com.example.project.data.model.incident.MeasuredIncident;
import com.example.project.data.model.incident.MinIncident;

import java.util.ArrayList;
import java.util.List;

public class WeatherReport {
    @Embedded
    public Report report;

    @Relation(parentColumn = "id", entityColumn = "report_id")
    private List<MinIncident> minIncidentList;

    @Relation(parentColumn = "id", entityColumn = "report_id")
    private List<BasicIncident> basicIncidentList;

    @Relation(parentColumn = "id", entityColumn = "report_id")
    private List<MeasuredIncident> measuredIncidentList;

    public WeatherReport(Report report, List<MinIncident> minIncidentList,
                         List<BasicIncident> basicIncidentList,
                         List<MeasuredIncident> measuredIncidentList) {
        this.report = report;
        this.minIncidentList = minIncidentList;
        this.basicIncidentList = basicIncidentList;
        this.measuredIncidentList = measuredIncidentList;
    }

    public Report getReport() {
        return report;
    }

    public List<MinIncident> getMinIncidentList() {
        return minIncidentList;
    }

    public List<BasicIncident> getBasicIncidentList() {
        return basicIncidentList;
    }

    public List<MeasuredIncident> getMeasuredIncidentList() {
        return measuredIncidentList;
    }


    public ArrayList<Incident> getIncidentList() {
        ArrayList<Incident> incidents = new ArrayList<>();
        incidents.addAll(basicIncidentList);
        incidents.addAll(measuredIncidentList);
        //minIncident missing
        return incidents;
    }
}
