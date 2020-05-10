package com.example.project.main.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.model.Tweet;
import com.example.project.main.TwitterActivity;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.Set;

public class SettingsFragment extends PreferenceFragmentCompat {
    public final static int REQUEST_CONNECT_TWITTER = 11;
    private SettingsViewModel viewModel;
    private Gson gson = new Gson();


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        viewModel = new SettingsViewModel(Objects.requireNonNull(getActivity()).getApplication());
        EditTextPreference editTextBug = findPreference(getString(R.string.key_bug));
        SwitchPreference spTwitter = findPreference(getString(R.string.key_twitter));
        ListPreference lpArea = findPreference(getString(R.string.key_area));
        MultiSelectListPreference mlpIncident = findPreference(getString(R.string.key_incident));
        SwitchPreference spGps = findPreference(getString(R.string.key_coordinates));
        SwitchPreference spSound = findPreference(getString(R.string.key_sound));
        SwitchPreference spNotifications = findPreference(getString(R.string.key_notifications));

        assert lpArea != null ; assert mlpIncident != null; assert spGps != null ;
        assert spSound != null ; assert spNotifications != null ; assert  spTwitter != null;
        assert editTextBug != null;
        editTextBug.setOnBindEditTextListener(editText -> viewModel.sendBugReport(editText.getText().toString()));

        Objects.requireNonNull(editTextBug).setText("");
        Objects.requireNonNull(spTwitter).setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), TwitterActivity.class);
            if (spTwitter.isChecked()) {
                appendProgressCircle(View.VISIBLE);
                intent.putExtra(TwitterActivity.TWITTER_ACTION, TwitterActivity.TWITTER_CONNECT);
                startActivityForResult(intent, REQUEST_CONNECT_TWITTER);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.button_confirm, (dialog, id) -> {
                    viewModel.setTwitterAccount(null);
                    Toast.makeText(getContext(), getString(R.string.account_disconnected), Toast.LENGTH_SHORT).show();

                });

                builder.setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                    //TWEET TEST
                    Tweet tw = new Tweet("This is a test", "#SeaReports", "#Polytech");
                    spTwitter.setChecked(true);
                    startActivity(TwitterActivity.tweetIntentBuilder(intent, gson.toJson(tw), viewModel.getTwitterToken()));
                });

                builder.setTitle(R.string.title_twitter_log_out);
                builder.setMessage(R.string.message_twitter_log_out);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            return false;
        });

        //update the view
        viewModel.getSettingsModelMutableLiveData().observe(this, newSettings -> {
            lpArea.setValue(newSettings.getReportsArea());
            mlpIncident.setValues(newSettings.getIncidentFilter());
            spGps.setChecked(newSettings.isDisplayCoordinatesOn());
            spSound.setChecked(newSettings.isSoundOn());
            spNotifications.setChecked(newSettings.isIncidentNotificationOn());
            spTwitter.setChecked(newSettings.hasTwitterAccount());
        });

        //update the modelView
        lpArea.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                viewModel.setReportsArea(newValue.toString());
                return false;
            }
        });

        mlpIncident.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                viewModel.setIncidentFilter((Set<String>) newValue);
                return false;
            }
        });

        spGps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                viewModel.setDisplayCoordinates(Boolean.parseBoolean(newValue.toString()));
                return false;
            }
        });

        spNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                viewModel.setIncidentNotificationOn(Boolean.parseBoolean(newValue.toString()));
                return false;
            }
        });

        spSound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                viewModel.setSoundOn(Boolean.parseBoolean(newValue.toString()));
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        appendProgressCircle(View.GONE);
        if (requestCode == REQUEST_CONNECT_TWITTER) {
            if (resultCode == Activity.RESULT_CANCELED) {
                SwitchPreference sp_twitter = findPreference(getString(R.string.key_twitter));
                assert sp_twitter != null;
                sp_twitter.setChecked(false);
            } else if(resultCode==Activity.RESULT_OK) {
                viewModel.setTwitterAccount(data.getStringExtra(TwitterActivity.TWITTER_SESSION));
                Toast.makeText(getContext(), "Hello " + viewModel.getCurrentSettings().getTwitterAccount().getUserName() + ", your account has been successfully added.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void appendProgressCircle(int state){
        Objects.requireNonNull(getActivity()).findViewById(R.id.loadingPanel).setVisibility(state);
    }

}