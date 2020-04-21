package com.example.project.main.factory;

import com.example.project.R;
import com.example.project.data.model.incident.Incident;
import com.example.project.data.model.information.Information;
import com.example.project.types.ITypeIncident;

public abstract class IncidentFactory {

    static final int INCIDENT_BASIC = ITypeIncident.INCIDENT_BASIC;
    static final int INCIDENT_UNIT = ITypeIncident.INCIDENT_UNIT;

    static final int TEMPERATURE = ITypeIncident.TEMPERATURE;
    static final int RAIN = ITypeIncident.RAIN;
    static final int HAIL = ITypeIncident.HAIL;
    static final int FOG = ITypeIncident.FOG;
    static final int CLOUD = ITypeIncident.CLOUD;
    static final int STORM = ITypeIncident.STORM;
    static final int WIND = ITypeIncident.WIND;
    static final int CURRENT = ITypeIncident.CURRENT;
    static final int TRANSPARENCY = ITypeIncident.TRANSPARENCY;
    static final int OTHER = ITypeIncident.OTHER;

    //TODO use dimen values !! but how to do in a final value?
    /*public static final int INCIDENT_BASIC = R.dimen.inc_basic;
    public static final int INCIDENT_UNIT = R.dimen.inc_unit;

    public static final int SPEC_GENERAL = R.dimen.inc_basic;
    public static final int SPEC_MIN = R.dimen.inc_unit;
    public static final int SPEC_UNIT = R.dimen.inc_unit;

    public static final int TEMPERATURE = R.dimen.info_temperature;
    public static final int RAIN = R.dimen.info_rain;
    public static final int HAIL = R.dimen.info_hail;
    public static final int FOG = R.dimen.info_fog;
    public static final int CLOUD = R.dimen.info_cloud;
    public static final int STORM = R.dimen.info_storm;
    public static final int WIND = R.dimen.info_wind;
    public static final int CURRENT = R.dimen.info_current;
    public static final int TRANSPARENCY = R.dimen.info_transparency;
    public static final int OTHER = R.dimen.info_other;*/


    protected abstract Incident buildIncident(int typeIncident, int typeInformation, String comment) throws Throwable;
    protected abstract Information buildInformation(int typeIncident, int typeInformation, int icon, String value) throws Throwable;
}
