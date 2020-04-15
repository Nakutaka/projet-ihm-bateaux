package com.example.roomlocalreport.localdb.models;

import android.telephony.mbms.MbmsErrors;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class ReportWithIncidents {
   @Embedded
   public Report report;
   @Relation(
           parentColumn = "report_id",
           entityColumn = "i_report_id"
   )
   public List<Incident> incidents;

   public ReportWithIncidents(Report report, List<Incident> incidents) {
      this.report = report;
      this.incidents = incidents;
   }

   @Ignore
   public ReportWithIncidents(long time, double latitude, double longitude) {
      this.report = new Report(time, latitude, longitude);
      this.incidents = new ArrayList<Incident>();
   }

   @Ignore
   public ReportWithIncidents(Report report) {
      this.report = report;
      this.incidents = new ArrayList<Incident>();
   }

   public void addIncident(Incident incident){
      incidents.add(incident);
   }

   public void addIncident(long reportId, long incidentId, String comment){
      incidents.add(new Incident(reportId, incidentId, comment));
   }

   public void addIncident(long incidentId, String comment){
      incidents.add(new Incident(incidentId, comment));
   }

   public void setReportIdToIncident(long reportId){
      incidents.forEach(i -> i.setReportId(reportId));
   }
}
