package com.example.projet.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = {"i_report_id", "incident_id"})//(indices = {@Index("i_report_id")}, foreignKeys = @ForeignKey(entity = Report.class, parentColumns = "report_id", childColumns = "i_report_id", onDelete = CASCADE))
public class Incident {
   //@PrimaryKey//(autoGenerate = true)
   @ColumnInfo(name = "incident_id")
   public long incidentId;

   @ColumnInfo(name = "i_report_id")
   public long reportId;

   public String comment;
   //public int type;//for now --> then each incidentType extends Incident class
   //class Rain/Hurricane/Transparency/etc... extends Incident

   @Ignore
   public Incident(long incidentId, String comment) {//, int type) {
      this.incidentId = incidentId;
      this.comment = comment;
      //this.type = type;
   }

   public Incident(long reportId, long incidentId, String comment) {//, int type) {
      this.reportId = reportId;
      this.incidentId = incidentId;
      this.comment = comment;
      //this.type = type;
   }

   public void setReportId(long reportId) { this.reportId = reportId; }

   public long getIncidentId() { return this.incidentId; }
   public long getReportId() { return this.reportId; }
   //public long getReportId() { return this.reportId; }
   public String getComment() { return this.comment; }
   //public int getType() { return this.type; }
}
