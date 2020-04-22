package com.example.project.data.model.incident;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project.data.model.information.Information;

public class Incident implements Parcelable {
    private int type;
    private String comment;
    private Information info;

    public Incident(int type, String comment, Information info) {
        this.type = type;
        this.comment = comment;
        this.info = info;
    }

    public Incident(int type, String comment) {
        this.type = type;
        this.comment = comment;
        info = null;
    }

    protected Incident(Parcel in) {
        type = in.readInt();
        comment = in.readString();
    }

    public static final Creator<Incident> CREATOR = new Creator<Incident>() {
        @Override
        public Incident createFromParcel(Parcel in) {
            return new Incident(in);
        }

        @Override
        public Incident[] newArray(int size) {
            return new Incident[size];
        }
    };

    public int getType() { return this.type; }
    public String  getComment() { return this.comment; }

    public void setInformationOnce(Information info) {
        if(this.info == null) this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(comment);
    }
}
