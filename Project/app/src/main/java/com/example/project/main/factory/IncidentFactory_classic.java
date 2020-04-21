package com.example.project.main.factory;

import com.example.project.data.model.incident.BasicIncident;
import com.example.project.data.model.incident.Incident;
import com.example.project.data.model.incident.UnitIncident;
import com.example.project.data.model.information.GeneralInformation;
import com.example.project.data.model.information.Information;
import com.example.project.data.model.information.MinimumInformation;
import com.example.project.data.model.information.SpecificInformation;

public class IncidentFactory_classic extends IncidentFactory {//extends IncidentFactory {

    @Override
    public Incident buildIncident(int typeIncident, int typeInformation, String comment)  throws Throwable {
        switch(typeIncident) {
            case IncidentFactory.INCIDENT_BASIC:
                switch(typeInformation) {
                    case IncidentFactory.CLOUD:
                    case IncidentFactory.RAIN:
                    case IncidentFactory.STORM:
                    case IncidentFactory.FOG:
                    case IncidentFactory.HAIL:
                    case IncidentFactory.CURRENT: return new BasicIncident(typeIncident, comment);
                    default: throw new Throwable("basic incident incompatible with information type");
                }
            case IncidentFactory.INCIDENT_UNIT:
                switch(typeInformation) {
                    case IncidentFactory.TEMPERATURE:
                    case IncidentFactory.WIND:
                    case IncidentFactory.TRANSPARENCY: return new UnitIncident(typeIncident, comment);
                }
                throw new Throwable("unit incident incompatible with information type");
            default: throw new Throwable("incident type not made");
        }
    }

    @Override
    public Information buildInformation(int typeIncident, int typeInformation, int icon, String value) throws Throwable {
        switch(typeInformation) {
            case IncidentFactory.TEMPERATURE:
            case IncidentFactory.WIND:
            case IncidentFactory.TRANSPARENCY:
                if(typeIncident == IncidentFactory.INCIDENT_UNIT) return new SpecificInformation(
                        typeInformation, icon, value);
                throw new Throwable("specific information incompatible with incident type");
            case IncidentFactory.CLOUD:
            case IncidentFactory.RAIN:
            case IncidentFactory.STORM:
            case IncidentFactory.FOG:
            case IncidentFactory.HAIL:
            case IncidentFactory.CURRENT:
                if(typeIncident == IncidentFactory.INCIDENT_BASIC) return new GeneralInformation(
                        typeInformation, icon, value);
                throw new Throwable("general information incompatible with incident type");
            case IncidentFactory.OTHER:
                if(typeIncident == IncidentFactory.INCIDENT_BASIC) return new MinimumInformation(
                        typeInformation, icon);
                throw new Throwable("minimum information incompatible with incident type");
            default: throw new Throwable("information type not made");
        }
    }
}
