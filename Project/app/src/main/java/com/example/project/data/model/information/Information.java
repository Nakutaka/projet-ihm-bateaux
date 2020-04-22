package com.example.project.data.model.information;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project.R;

public class Information implements Parcelable {
    private int type;
    private int icon;

    public Information(int type, int icon) {
        this.type = type;
        this.icon = icon;
    }

    public Information(int type) {
        this.type = type;
        this.icon = findIcon(type);
    }

    protected Information(Parcel in) {
        type = in.readInt();
        icon = in.readInt();
    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    private int findIcon(int type) {
        switch(type) {
            case R.dimen.info_temperature: return R.drawable.ic_temperature;
            case R.dimen.info_rain: return R.drawable.ic_rain;
            case R.dimen.info_hail: return R.drawable.ic_hail;
            case R.dimen.info_fog: return R.drawable.ic_fog;
            case R.dimen.info_cloud: return R.drawable.ic_cloud;
            case R.dimen.info_storm: return R.drawable.ic_storm;
            case R.dimen.info_wind: return R.drawable.ic_wind;
            case R.dimen.info_current: return R.drawable.ic_current;
            case R.dimen.info_transparency: return R.drawable.ic_transparency;
            case R.dimen.info_other: return R.drawable.ic_other;
            default: return -1;
        }
    }

    public int getType() { return this.type; }
    public int getIcon() { return this.icon; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeInt(icon);
    }
}
