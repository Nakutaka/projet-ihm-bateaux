package com.example.project.data.model.entities;

import androidx.room.Entity;

@Entity
public class Transparency extends IncidentDBUnit {
    public Transparency(long reportId, double latitude, double longitude, int incidentId,
                        int icon, String comment, String value, String unit) {
        super(reportId, latitude, longitude, incidentId, icon, comment, value, unit);
    }
}
