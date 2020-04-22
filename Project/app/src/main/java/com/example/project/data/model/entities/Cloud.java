package com.example.project.data.model.entities;

import androidx.room.Entity;

@Entity
public class Cloud extends IncidentDBBasic {

    public Cloud(long reportId, double latitude, double longitude, int incidentId,
                 int icon, String comment, String level) {
        super(reportId, latitude, longitude, incidentId, icon, comment, level);
    }
}
