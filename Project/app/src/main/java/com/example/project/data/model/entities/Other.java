package com.example.project.data.model.entities;

import androidx.room.Entity;

@Entity
public class Other extends IncidentDB {

    public Other(long reportId, double latitude, double longitude, int incidentId,
                 int icon, String comment) {
        super(reportId, latitude, longitude, incidentId, icon, comment);
    }
}
