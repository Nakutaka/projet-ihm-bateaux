package com.example.project.data.model.weather.local;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Info implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int infoId;
    public String name;
    public String icon;

    public Info(int infoId, String name, String icon) {
        this.infoId = infoId;
        this.name = name;
        this.icon = icon;
    }

    @Ignore
    public Info(String name, String icon) {
        this.infoId = -1;
        this.name = name;
        this.icon = icon;
    }

    protected Info(Parcel in) {
        name = in.readString();
        icon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public int getInfoId() {
        return infoId;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
