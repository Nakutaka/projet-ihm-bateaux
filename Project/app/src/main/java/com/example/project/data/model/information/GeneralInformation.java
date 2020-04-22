package com.example.project.data.model.information;

import android.os.Parcel;

public class GeneralInformation extends Information {
    private String level;

    public GeneralInformation(int type, int icon, String level) {
        super(type, icon);
        this.level = level;
    }

    public GeneralInformation(int type, String level) {
        super(type);
        this.level = level;
    }

    protected GeneralInformation(Parcel in) {
        super(in);
        level = in.readString();
    }

    public String getLevel() { return this.level; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(level);
    }
}
