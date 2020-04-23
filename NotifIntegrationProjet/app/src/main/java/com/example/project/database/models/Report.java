package com.example.projet.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(primaryKeys = {"report_id", "latitude", "longitude"})
public class Report implements Parcelable {
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

   public Report(Parcel in) {
      reportId = in.readLong();
      latitude = in.readDouble();
      longitude = in.readDouble();
   }

   public static final Creator<Report> CREATOR = new Creator<Report>() {
      @Override
      public Report createFromParcel(Parcel in) { return new Report(in); }

      @Override
      public Report[] newArray(int size) { return new Report[size]; }
   };

   @Override
   public int describeContents() { return 0; }

   @Override
   public void writeToParcel(Parcel parcel, int i) {
      parcel.writeLong(reportId);
      parcel.writeDouble(latitude);
      parcel.writeDouble(longitude);
   }

   public long getReportId() { return this.reportId; }
   public double getLatitude() { return this.latitude; }
   public double getLongitude() { return this.longitude; }

   public void setNewReportId(long reportId) { this.reportId = reportId; }
}

