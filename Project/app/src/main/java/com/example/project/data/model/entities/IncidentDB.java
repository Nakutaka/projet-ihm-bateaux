package com.example.project.data.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"report_id", "latitude", "longitude", "incident_id"})//(indices = {@Index("i_report_id")}, foreignKeys = @ForeignKey(entity = Report.class, parentColumns = "report_id", childColumns = "i_report_id", onDelete = CASCADE))
public class IncidentDB {
   @ColumnInfo(name = "report_id")
   public long reportId;
   public double latitude;
   public double longitude;
   @ColumnInfo(name = "incident_id")
   public int incidentId;//type
   public int icon;//icon id
   public String comment;

   public IncidentDB(long reportId, double latitude, double longitude, int incidentId, int icon, String comment) {
      this.reportId = reportId;
      this.latitude = latitude;
      this.longitude = longitude;
      this.incidentId = incidentId;
      this.icon = icon;
      this.comment = comment;
   }

   public long getReportId() { return this.reportId; }
   public double getLatitude() { return this.latitude; }
   public double getLongitude() { return this.longitude; }
   public int getIncidentId() { return this.incidentId; }
   public int getIcon() { return this.icon; }
   public String getComment() { return this.comment; }
}
