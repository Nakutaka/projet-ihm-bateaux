package com.example.projet.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportWithIncidents {
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
