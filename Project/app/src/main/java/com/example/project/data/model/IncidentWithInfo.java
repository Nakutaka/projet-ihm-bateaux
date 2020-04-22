package com.example.project.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project.data.model.incident.Incident;
import com.example.project.data.model.information.Information;

public class IncidentWithInfo implements Parcelable {
    private Incident incident;
    private Information info;

    public IncidentWithInfo(Incident incident, Information info) {
        this.incident = incident;
        this.info = info;
    }

    protected IncidentWithInfo(Parcel in) {
        incident = in.readParcelable(Incident.class.getClassLoader());
        info = in.readParcelable(Information.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(incident, flags);
        dest.writeParcelable(info, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IncidentWithInfo> CREATOR = new Creator<IncidentWithInfo>() {
        @Override
        public IncidentWithInfo createFromParcel(Parcel in) {
            return new IncidentWithInfo(in);
        }

        @Override
        public IncidentWithInfo[] newArray(int size) {
            return new IncidentWithInfo[size];
        }
    };

    public Incident getIncident() { return this.incident; }
    public Information getInfo() { return this.info; }
}
