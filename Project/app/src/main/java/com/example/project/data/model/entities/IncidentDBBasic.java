package com.example.project.data.model.entities;

public class IncidentDBBasic extends IncidentDB {
    public String level;

    public IncidentDBBasic(long reportId, double latitude, double longitude, int incidentId,
                           int icon, String comment, String level) {
        super(reportId, latitude, longitude, incidentId, icon, comment);
        this.level = level;
    }

    public String getLevel() { return level; }
}
