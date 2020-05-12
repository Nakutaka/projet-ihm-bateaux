package com.example.project.main.factory;

import android.telephony.TelephonyManager;

import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.info.Cloud;
import com.example.project.model.weather.local.info.Current;
import com.example.project.model.weather.local.info.Fog;
import com.example.project.model.weather.local.info.Hail;
import com.example.project.model.weather.local.Incident;
import com.example.project.model.weather.local.Info;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.example.project.model.weather.local.info.Other;
import com.example.project.model.weather.local.info.Rain;
import com.example.project.model.weather.Report;
import com.example.project.model.weather.local.info.Storm;
import com.example.project.model.weather.local.info.Temperature;
import com.example.project.model.weather.local.info.Transparency;
import com.example.project.model.weather.local.info.Wind;

public class IncidentFactory_classic extends IncidentFactory {

    @Override
    public Report getReport(String deviceId, double latitude, double longitude) {
        return new Report(deviceId, false, latitude, longitude);//temporary for now
    }

    @Override
    public Incident getIncident(int typeIncident, int typeInformation, String value, String unit, String comment) {
        try {
            Incident incident = buildIncident(typeIncident, typeInformation, value, unit, comment);
            incident.setInfo(buildInformation(typeIncident, typeInformation));
            return incident;
        }
        catch(Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    protected Incident buildIncident(int typeIncident, int typeInformation, String value, String unit, String comment) throws Throwable {
        switch (typeIncident) {
            case IncidentFactory.INCIDENT_MIN:
                switch (typeInformation) {
                    case IncidentFactory.OTHER:
                        return new MinIncident(typeInformation % nb, comment);
                }
            case IncidentFactory.INCIDENT_BASIC:
                switch (typeInformation) {
                    case IncidentFactory.CLOUD:
                    case IncidentFactory.RAIN:
                    case IncidentFactory.STORM:
                    case IncidentFactory.FOG:
                    case IncidentFactory.HAIL:
                    case IncidentFactory.CURRENT:
                        return new BasicIncident(typeInformation % 10, value, comment);
                    default:
                        throw new Throwable("basic incident incompatible with information type");
                }
            case IncidentFactory.INCIDENT_MEASURED:
                switch (typeInformation) {
                    case IncidentFactory.TEMPERATURE:
                    case IncidentFactory.WIND:
                    case IncidentFactory.TRANSPARENCY:
                        return new MeasuredIncident(typeInformation % 10, value, unit, comment);
                    default:
                        throw new Throwable("unit incident incompatible with information type");
                }
            default: throw new Throwable("incident type not made");
        }
    }

    @Override
    protected Info buildInformation(int typeIncident, int typeInformation) throws Throwable {
        switch(typeInformation) {
            case IncidentFactory.TEMPERATURE:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                    case IncidentFactory.INCIDENT_MEASURED:
                        return new Temperature();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.WIND:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                    case IncidentFactory.INCIDENT_MEASURED:
                        return new Wind();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.TRANSPARENCY:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                    case IncidentFactory.INCIDENT_MEASURED:
                        return new Transparency();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.CLOUD:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Cloud();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.RAIN:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Rain();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.STORM:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Storm();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.FOG:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Fog();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.HAIL:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Hail();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.CURRENT:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_BASIC:
                        return new Current();
                    default: throw new Throwable("information incompatible with incident type");
                }
            case IncidentFactory.OTHER:
                switch(typeIncident) {
                    case IncidentFactory.INCIDENT_MIN:
                        return new Other();
                    default: throw new Throwable("information incompatible with incident type");
                }
            default: throw new Throwable("information type not made");
        }
    }
}
