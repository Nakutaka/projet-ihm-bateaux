package com.example.project.data.model.entities;

import androidx.room.Entity;

@Entity
public class Fog extends IncidentDBBasic {

    public Fog(long reportId, double latitude, double longitude, int incidentId,
                 int icon, String comment, String level) {
        super(reportId, latitude, longitude, incidentId, icon, comment, level);
    }
}
