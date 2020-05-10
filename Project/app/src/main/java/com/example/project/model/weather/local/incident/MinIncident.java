package com.example.project.data.model.weather.local.incident;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.project.data.model.weather.local.Incident;
import com.example.project.data.model.weather.local.Info;

@Entity
public class MinIncident extends Incident implements Parcelable {
    public String comment;

    @Ignore
    public MinIncident(int num, String comment) {
        this.num = num;
        this.info = null;
        this.comment = comment;
    }

    public MinIncident(String reportId, int num, Info info, String comment) {
        this.reportId = reportId;
        this.num = num;
        this.info = info;
        this.comment = comment;
    }

    private MinIncident(Parcel in) {
        super(in);
        comment = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MinIncident> CREATOR = new Creator<MinIncident>() {
        @Override
        public MinIncident createFromParcel(Parcel in) {
            return new MinIncident(in);
        }

        @Override
        public MinIncident[] newArray(int size) {
            return new MinIncident[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(comment);
    }

    public String getComment() {
        return comment;
    }
}
