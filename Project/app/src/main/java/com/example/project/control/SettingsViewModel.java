package com.example.project.control;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.project.R;
import com.example.project.data.model.SettingsModel;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SettingsViewModel extends AndroidViewModel {
    public static String SHARED_PREFERENCES = "prefs";

    /* DEFAULT VALUES */
    private final Set<String> incidentFilterDefault = new HashSet<>(Arrays.asList(getStringArray(R.array.incident_entries)));
    private final String reportsAreaDefault = getString(R.string.pref_area_100_value);
    private final boolean soundDefault = getBool(R.bool.pref_sound_on);
    private final boolean incidentNotificationDefault = getBool(R.bool.pref_notifications_on);
    private final boolean displayCoordinatesDefault = getBool(R.bool.pref_coordinates_displayed);


    private SettingsModel currentSettings;
    private SharedPreferences sharedPref;
    private MutableLiveData<SettingsModel> settingsModelMutableLiveData = new MutableLiveData<>();
    private Gson gson = new Gson();


    public SettingsViewModel(Application application){
        super(application);
        sharedPref = getApplication().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        initSettings();
    }

    private void initSettings() {
        Set<String> incidentsFilter = sharedPref.getStringSet(getString(R.string.key_incident), incidentFilterDefault);
        String twitterObj = sharedPref.getString(getString(R.string.key_twitter), "null");
        TwitterSession twitterSession = null;
        if(!twitterObj.equals("null")) {
            twitterSession = gson.fromJson(twitterObj, TwitterSession.class);
            if(twitterSession.getAuthToken().isExpired()) twitterSession = null;
        }
        String reportsArea = sharedPref.getString(getString(R.string.key_area), reportsAreaDefault);
        boolean sounds = sharedPref.getBoolean(getString(R.string.key_sound), soundDefault);
        boolean notifications = sharedPref.getBoolean(getString(R.string.key_notifications), incidentNotificationDefault);
        boolean coordinates = sharedPref.getBoolean(getString(R.string.key_coordinates), displayCoordinatesDefault);
        currentSettings = new SettingsModel(incidentsFilter, twitterSession, reportsArea, sounds, notifications, coordinates);
        settingsModelMutableLiveData.setValue(currentSettings);
    }

    private void saveSettings(SettingsModel newSettings){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(getString(R.string.key_incident), newSettings.getIncidentFilter());
        editor.putString(getString(R.string.key_area), newSettings.getReportsArea());
        editor.putBoolean(getString(R.string.key_sound), newSettings.isSoundOn());
        editor.putBoolean(getString(R.string.key_notifications), newSettings.isIncidentNotificationOn());
        editor.putBoolean(getString(R.string.key_coordinates), newSettings.isDisplayCoordinatesOn());
        editor.putString(getString(R.string.key_twitter), gson.toJson(newSettings.getTwitterAccount()));
        editor.apply();
        settingsModelMutableLiveData.setValue(newSettings);
    }


    private String getString(int id) {
        return getApplication().getResources().getString(id);
    }

    private String[] getStringArray(int id){
        return getApplication().getResources().getStringArray(id);
    }

    private boolean getBool(int id){
        return getApplication().getResources().getBoolean(id);
    }


    public SettingsModel getCurrentSettings(){
        return currentSettings;
    }

    public void setReportsArea(String value) {
        currentSettings.setReportsArea(value);
        saveSettings(currentSettings);
    }

    public void setTwitterAccount(TwitterSession twitterSession) {
        currentSettings.setTwitterAccount(twitterSession);
        saveSettings(currentSettings);
    }

    public void setIncidentFilter(Set<String> incidentsFilter) {
        currentSettings.setIncidentFilter(incidentsFilter);
        saveSettings(currentSettings);
    }

    public void setIncidentNotificationOn(boolean value) {
        currentSettings.setIncidentNotificationOn(value);
        saveSettings(currentSettings);
    }

    public void setSoundOn(boolean value) {
        currentSettings.setSoundOn(value);
        saveSettings(currentSettings);
    }

    public void setDisplayCoordinates(boolean value) {
        currentSettings.setDisplayCoordinatesOn(value);
        saveSettings(currentSettings);
    }

    public MutableLiveData<SettingsModel> getSettingsModelMutableLiveData(){
        return settingsModelMutableLiveData;
        }

    public void sendBugReport(String bugReport) {
        //TODO
    }
}
