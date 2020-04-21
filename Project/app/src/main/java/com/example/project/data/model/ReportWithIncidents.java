package com.example.project.data.model;

import com.example.project.data.model.entities.Report;

import java.util.List;

public class ReportWithIncidents {

   private Report report;
   private List<IncidentWithInfo> incidents;

   public ReportWithIncidents(Report report, List<IncidentWithInfo> incidents) {
      this.report = report;
      incidents = incidents;
   }

   public Report getReport() { return report; }
   public List<IncidentWithInfo> getIncidents() { return incidents; }
}
