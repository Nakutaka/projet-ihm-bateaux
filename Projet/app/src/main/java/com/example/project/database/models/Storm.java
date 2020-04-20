package com.example.projet.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

@Entity
public class Storm extends Incident implements Parcelable {
    // @ColumnInfo(name = "field_name")
    //public TYPE FIELD;

    public Storm(long reportId, int incidentId, String comment) {
        super(reportId, incidentId, comment);
    }

    protected Storm(Parcel in) {
        super(in);
    }

    public static final Creator<Storm> CREATOR = new Creator<Storm>() {
        @Override
        public Storm createFromParcel(Parcel in) {
            return new Storm(in);
        }

        @Override
        public Storm[] newArray(int size) {
            return new Storm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        //write new fields
    }


    //public TYPE getFIELD() { return this.FIELD; }
}
