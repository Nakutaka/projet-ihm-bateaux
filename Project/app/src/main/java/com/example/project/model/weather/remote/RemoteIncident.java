package com.example.project.model.weather.remote;

import com.google.gson.annotations.SerializedName;

public class RemoteIncident {

    @SerializedName("type_incident")
    public int typeIncident;
    @SerializedName("type_information")
    public int typeInfo;
    public String value;
    public String unit;
    public String comment;

    //getIncident(int typeIncident, int typeInformation, String value, String unit, String comment);
    public RemoteIncident(int typeIncident, int typeInfo, String value, String unit, String comment) {
        this.typeIncident = typeIncident;
        this.typeInfo = typeInfo;
        this.value = value;
        this.unit = unit;
        this.comment = comment;
    }

    public int getTypeIncident() {
        return typeIncident;
    }

    public int getTypeInfo() {
        return typeInfo;
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

    public void setTypeIncident(int typeIncident) {
        this.typeIncident = typeIncident;
    }

    public void setTypeInfo(int typeInfo) {
        this.typeInfo = typeInfo;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
