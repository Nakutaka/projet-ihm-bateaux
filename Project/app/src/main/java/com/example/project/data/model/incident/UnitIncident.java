package com.example.project.data.model.incident;

import android.os.Parcelable;

import com.example.project.data.model.information.Information;

public class UnitIncident extends Incident implements Parcelable {

    public UnitIncident(int type, String comment, Information info) {
        super(type, comment, info);
    }

    public UnitIncident(int type, String comment) {
        super(type, comment);
    }
}
