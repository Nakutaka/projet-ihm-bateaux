package com.example.project.model;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.HashSet;
import java.util.Set;

public class SettingsModel {
    /* Type of incidents that the user would like to see on the map */
    private Set<String> incidentFilter = new HashSet<>();
    /* Contains token */
    private TwitterSession twitterSession;
    /* The area around the user where incidents should appear */
    private String reportsArea;
    /* Sound for notifications */
    private boolean soundOn;
    /* Notification of new incidents */
    private boolean incidentNotificationOn;
    /* Always display the GPS coordinates on the map */
    private boolean displayCoordinatesOn;


    public SettingsModel(Set<String> incidentFilter, TwitterSession twitterSession,
                         String reportsArea, boolean soundOn, boolean incidentNotificationOn,
                         boolean displayCoordinatesOn) {
        this.incidentFilter = incidentFilter;
        this.twitterSession = twitterSession;
        this.reportsArea = reportsArea;
        this.soundOn = soundOn;
        this.incidentNotificationOn = incidentNotificationOn;
        this.displayCoordinatesOn = displayCoordinatesOn;
    }


    public TwitterSession getTwitterAccount() {
        return twitterSession;
    }

    public void setTwitterAccount(TwitterSession twitterAccount) {
        this.twitterSession = twitterAccount;
    }

    public boolean hasTwitterAccount(){
        return twitterSession != null && !twitterSession.getAuthToken().isExpired();
    }

    public Set<String> getIncidentFilter() {
        return incidentFilter;
    }

    public void setIncidentFilter(Set<String> incidentFilter) {
        this.incidentFilter = incidentFilter;
    }

    public String getReportsArea(){
        return reportsArea;
    }

    public void setReportsArea(String reportsArea){
        this.reportsArea = reportsArea;
    }

    public boolean isSoundOn(){
        return soundOn;
    }

    public void setSoundOn(boolean soundOn){
        this.soundOn = soundOn;
    }

    public boolean isIncidentNotificationOn(){
        return incidentNotificationOn;
    }

    public void setIncidentNotificationOn(boolean notificationOn){
        this.incidentNotificationOn = notificationOn;
    }

    public boolean isDisplayCoordinatesOn() {
        return displayCoordinatesOn;
    }

    public void setDisplayCoordinatesOn(boolean displayCoordinatesOn) {
        this.displayCoordinatesOn = displayCoordinatesOn;
    }

}
