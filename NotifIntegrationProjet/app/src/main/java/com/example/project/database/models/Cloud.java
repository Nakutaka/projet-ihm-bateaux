package com.example.projet.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class Cloud extends Incident implements Parcelable {
    // @ColumnInfo(name = "field_name")
    //public TYPE FIELD;

    public Cloud(long reportId, int incidentId, String comment) {
        super(reportId, incidentId, comment);
    }

    protected Cloud(Parcel in) {
        super(in);
    }

    @Ignore
    public Cloud(Cloud cloud) {
        this(cloud.reportId, cloud.incidentId, cloud.comment);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        //the rest
    }

    //public TYPE getFIELD() { return this.FIELD; }
}
