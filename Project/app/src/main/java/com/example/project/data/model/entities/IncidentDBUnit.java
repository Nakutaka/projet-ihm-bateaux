package com.example.project.data.model.entities;

public class IncidentDBUnit extends IncidentDB {
    public String value;
    public String unit;

    public IncidentDBUnit(long reportId, double latitude, double longitude, int incidentId,
                           int icon, String comment, String value, String unit) {
        super(reportId, latitude, longitude, incidentId, icon, comment);
        this.value = value;
        this.unit = unit;
    }

    public String getValue() { return value; }
    public String getUnit() { return unit; }
}
