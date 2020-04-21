package com.example.project.data.model.entities;

import androidx.room.Entity;

@Entity
public class Storm extends IncidentDBBasic {

    public Storm(long reportId, double latitude, double longitude, int incidentId,
                 int icon, String comment, String level) {
        super(reportId, latitude, longitude, incidentId, icon, comment, level);
    }
}
