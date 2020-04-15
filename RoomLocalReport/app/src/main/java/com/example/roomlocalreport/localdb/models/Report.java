package com.example.roomlocalreport.localdb.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Report {
   /*@PrimaryKey//(autoGenerate = true)
   @ColumnInfo(name = "report_id") public long reportId;*/
   //public Date date;
   // from Location class:
   @PrimaryKey//(autoGenerate = true)
   @ColumnInfo(name = "report_id")// public long reportId;
   public long reportId;//time;//getTime()

   public double latitude;//getLatitude()
   public double longitude;//getLongitude()

   @Ignore
   public Report(long reportId, double latitude, double longitude) {
      this.reportId = reportId;
      //this.time = time;
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public Report(long reportId) {
      this.reportId = reportId;
   }

   public long getReportId() { return this.reportId; }//time; }
   //public long getTime() { return this.time; }
   public double getLatitude() { return this.latitude; }
   public double getLongitude() { return this.longitude; }
}

