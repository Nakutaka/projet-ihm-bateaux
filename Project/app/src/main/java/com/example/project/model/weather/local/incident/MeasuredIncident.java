package com.example.project.data.model.weather.local.incident;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.project.data.model.weather.local.Incident;
import com.example.project.data.model.weather.local.Info;

@Entity
public class MeasuredIncident extends Incident implements Parcelable {
    public String value;
    public String unit;
    public String comment;

    @Ignore
    public MeasuredIncident(int num, String value, String unit, String comment) {
        this.num = num;
        this.info = null;
        this.value = value;
        this.unit = unit;
        this.comment = comment;
    }

    public MeasuredIncident(String reportId, int num, Info info, String value, String unit, String comment) {
        this.reportId = reportId;
        this.num = num;
        this.info = info;
        this.value = value;
        this.unit = unit;
        this.comment = comment;
    }

    private MeasuredIncident(Parcel in) {
        super(in);
        value = in.readString();
        unit = in.readString();
        comment = in.readString();
    }

    public static final Creator<MeasuredIncident> CREATOR = new Creator<MeasuredIncident>() {
        @Override
        public MeasuredIncident createFromParcel(Parcel in) {
            return new MeasuredIncident(in);
        }

        @Override
        public MeasuredIncident[] newArray(int size) {
            return new MeasuredIncident[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(value);
        dest.writeString(unit);
        dest.writeString(comment);
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getComment() {
        return comment;
    }
}

