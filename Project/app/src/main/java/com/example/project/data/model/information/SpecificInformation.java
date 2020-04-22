package com.example.project.data.model.information;

import android.os.Parcel;

import com.example.project.R;
import com.example.project.types.ITypeUnit;

public class SpecificInformation extends Information implements ITypeUnit {
    private String value;
    private String unit;

    public SpecificInformation(int type, int icon, String value) {
        super(type, icon);
        this.value = value;
        this.unit = unitSI(type);
    }

    public SpecificInformation(int type, String value) {
        super(type);
        this.value = value;
        this.unit = unitSI(type);
    }

    protected SpecificInformation(Parcel in) {
        super(in);
        value = in.readString();
        unit = in.readString();
    }

    private String unitSI(int type) {
        switch(type) {
            case R.dimen.info_temperature: return SI_UNIT_TEMP;//R.string.si_unit_degrees;
            case R.dimen.info_wind: return SI_UNIT_WIND;//R.string.si_unit_wind;
            case R.dimen.info_transparency: return SI_UNIT_DISTANCE;//R.string.si_unit_distance;
            default: return null;
        }
    }

    public String getValue() { return value; }
    public String getUnit() { return unit; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(value);
        parcel.writeString(unit);
    }
}