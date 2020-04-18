package com.example.projet.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

@Entity
public class Other extends Incident implements Parcelable {
    // @ColumnInfo(name = "field_name")
    //public TYPE FIELD;

    public Other(long reportId, int incidentId, String comment) {
        super(reportId, incidentId, comment);
    }

    protected Other(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        //the rest
    }

    //public TYPE getFIELD() { return this.FIELD; }
}