package com.example.projet.activities.forms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.R;
import com.example.projet.database.models.Cloud;
import com.example.projet.database.models.Current;
import com.example.projet.database.models.Fog;
import com.example.projet.database.models.Hail;
import com.example.projet.database.models.Incident;
import com.example.projet.database.models.Other;
import com.example.projet.database.models.Rain;
import com.example.projet.database.models.Storm;
import com.example.projet.database.models.Temperature;
import com.example.projet.database.models.Transparency;
import com.example.projet.database.models.Wind;
import com.example.projet.types.ITypeIncident;
import com.example.projet.types.ITypeParam;
import com.example.projet.fragments.incidents.CloudFragment;
import com.example.projet.fragments.incidents.CurrentFragment;
import com.example.projet.fragments.incidents.FogFragment;
import com.example.projet.fragments.incidents.HailFragment;
import com.example.projet.fragments.incidents.OtherFragment;
import com.example.projet.fragments.incidents.RainFragment;
import com.example.projet.fragments.incidents.StormFragment;
import com.example.projet.fragments.incidents.TemperatureFragment;
import com.example.projet.fragments.incidents.TransparencyFragment;
import com.example.projet.fragments.incidents.WindFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class IncidentFormActivity extends AppCompatActivity implements ITypeIncident {

    private long reportId;
    private int type;
    private String comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_form);

        Intent intent = getIntent();
        reportId = intent.getLongExtra(ITypeParam.REPORT_ACTIVITY_START, -1);
        type = getIntent().getIntExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, 0);
        displayFragment(type);

        findViewById(R.id.button_back).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.button_save).setOnClickListener(view -> {
            Intent result = new Intent();
            EditText editComment = findViewById(R.id.edit_comment);
            if(TextUtils.isEmpty(editComment.getText())) {// || type!=INCIDENT_CLOUD) {
                setResult(RESULT_CANCELED, result);
            }
            else{
                comment = editComment.getText().toString();
                Incident incident = newIncident();
                result.putExtra(ReportFormActivity.EXTRA_INCIDENT, incident);
                setResult(RESULT_OK, result);
            }
            finish();
        });
    }

    private void toastyIncident(String type, int id, String com) {
        Toast.makeText(
                getApplicationContext(), type + " - " + id + " - " + com,
                Toast.LENGTH_SHORT).show();
    }

    Incident newIncident() {
        switch(type) {
            case INCIDENT_TEMPERATURE: {
                toastyIncident("Temperature", INCIDENT_TEMPERATURE, comment);
                return new Temperature(reportId, type, comment);
            }
            case INCIDENT_RAIN: {
                toastyIncident("Rain", INCIDENT_RAIN, comment);
                return new Rain(reportId, type, comment);
            }
            case INCIDENT_HAIL: {
                toastyIncident("Hail", INCIDENT_HAIL, comment);
                return new Hail(reportId, type, comment);
            }
            case INCIDENT_FOG: {
                toastyIncident("Fog", INCIDENT_FOG, comment);
                return new Fog(reportId, type, comment);
            }
            case INCIDENT_CLOUD: {
                toastyIncident("Cloud", INCIDENT_CLOUD, comment);
                return new Cloud(reportId, type, comment);
            }
            case INCIDENT_STORM: {
                toastyIncident("Storm", INCIDENT_STORM, comment);
                return new Storm(reportId, type, comment);
            }
            case INCIDENT_WIND: {
                toastyIncident("Wind", INCIDENT_WIND, comment);
                return new Wind(reportId, type, comment);
            }
            case INCIDENT_CURRENT: {
                toastyIncident("Current", INCIDENT_CURRENT, comment);
                return new Current(reportId, type, comment);
            }
            case INCIDENT_TRANSPARENCY: {
                toastyIncident("Transparency", INCIDENT_TRANSPARENCY, comment);
                return new Transparency(reportId, type, comment);
            }
            default: {
                toastyIncident("Other", INCIDENT_OTHER, comment);
                return new Other(reportId, type, comment);
            }
        }
    }

    void displayFragment(int type) {
        switch(type) {
            case INCIDENT_TEMPERATURE: displayIncidentFragment(new TemperatureFragment()); break;
            case INCIDENT_RAIN: displayIncidentFragment(new RainFragment()); break;
            case INCIDENT_HAIL: displayIncidentFragment(new HailFragment()); break;
            case INCIDENT_FOG: displayIncidentFragment(new FogFragment()); break;
            case INCIDENT_CLOUD: displayIncidentFragment(new CloudFragment()); break;
            case INCIDENT_STORM: displayIncidentFragment(new StormFragment()); break;
            case INCIDENT_WIND: displayIncidentFragment(new WindFragment()); break;
            case INCIDENT_CURRENT: displayIncidentFragment(new CurrentFragment()); break;
            case INCIDENT_TRANSPARENCY: displayIncidentFragment(new TransparencyFragment()); break;
            default: displayIncidentFragment(new OtherFragment());//INCIDENT_OTHER
        }
    }

    void displayIncidentFragment(Fragment incidentFragment) {
        /*Bundle args = new Bundle();
        incidentFragment.setArguments(args);*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_incident,
                incidentFragment).commit();
    }
}