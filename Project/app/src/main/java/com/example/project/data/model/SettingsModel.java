package com.example.project.data.model;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel {

    private static double DEFAULT_ZOOM = 9.5;
    private double mapScale;
    private List<String> incidentFilter = new ArrayList<>();
    private String twitterAccount;


    public double getMapScale() {
        return mapScale;
    }

    public List<String> getIncidentFilter() {
        return incidentFilter;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setIncidentFilter(List<String> incidentFilter) {
        this.incidentFilter = incidentFilter;
    }

    public void setMapScale(double mapScale) {
        this.mapScale = mapScale;
    }

    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public boolean hasTwitterAccount(){
        return twitterAccount!=null;
    }
}
