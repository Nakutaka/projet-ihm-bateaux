package com.example.project.data.model.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReportWithIncidentsDB {
   @Embedded
   public Report report;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Cloud cloudIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Current currentIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Fog fogIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Hail hailIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Other otherIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Rain rainIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Storm stormIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Temperature temperatureIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Transparency transparencyIncident;

   @Relation(parentColumn = "report_id", entityColumn = "report_id")
   public Wind windIncident;
}
