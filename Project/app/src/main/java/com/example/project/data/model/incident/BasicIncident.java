package com.example.project.data.model.incident;

import android.os.Parcelable;

import com.example.project.data.model.information.Information;

public class BasicIncident extends Incident implements Parcelable {

    public BasicIncident(int type, String comment, Information info) {
        super(type, comment, info);
    }

    public BasicIncident(int type, String comment) {
        super(type, comment);
    }
}
