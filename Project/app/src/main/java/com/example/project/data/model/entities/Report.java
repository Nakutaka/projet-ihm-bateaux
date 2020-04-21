package com.example.project.data.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"report_id", "latitude", "longitude"})
public class Report {
  //@PrimaryKey//(autoGenerate = true)
   @ColumnInfo(name = "report_id")
   public long reportId;//getTime() - like in PS6
   public double latitude;//getLatitude()
   public double longitude;//getLongitude()

   public Report(long reportId, double latitude, double longitude) {
      this.reportId = reportId;
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public long getReportId() { return this.reportId; }
   public double getLatitude() { return this.latitude; }
   public double getLongitude() { return this.longitude; }
}

