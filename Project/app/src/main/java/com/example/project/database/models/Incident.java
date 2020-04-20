package com.example.project.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"report_id", "incident_id"})//(indices = {@Index("i_report_id")}, foreignKeys = @ForeignKey(entity = Report.class, parentColumns = "report_id", childColumns = "i_report_id", onDelete = CASCADE))
public class Incident implements Parcelable {
   @ColumnInfo(name = "report_id")
   public long reportId;
   @ColumnInfo(name = "incident_id")
   public int incidentId;//type
   public String comment;//class Rain/Hurricane/Transparency/etc... extends Incident

   public Incident(long reportId, int incidentId, String comment) {
      this.reportId = reportId;
      this.incidentId = incidentId;
      this.comment = comment;
   }

   public Incident(Parcel in) {
      reportId = in.readLong();
      incidentId = in.readInt();
      comment = in.readString();
   }

   public static final Creator<Incident> CREATOR = new Creator<Incident>() {
      @Override public Incident createFromParcel(Parcel in) { return new Incident(in); }

      @Override public Incident[] newArray(int size) { return new Incident[size]; }
   };

   @Override
   public int describeContents() { return 0; }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(reportId);
      dest.writeInt(incidentId);
      dest.writeString(comment);
   }

   public long getReportId() { return this.reportId; }
   public int getIncidentId() { return this.incidentId; }
   public String getComment() { return this.comment; }

   public void setNewReportId(long reportId) { this.reportId = reportId; }
}
