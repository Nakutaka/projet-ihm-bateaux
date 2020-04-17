package com.example.projet.activities;

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

public class IncidentActivity extends AppCompatActivity implements ITypeIncident {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        Intent intent = getIntent();
        long reportId = intent.getLongExtra(ITypeParam.REPORT_ACTIVITY_START, -1);
        int type = getIntent().getIntExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, 0);
        displayFragment(type);

        findViewById(R.id.button_back).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.button_save).setOnClickListener(view -> {
            Intent result = new Intent();
            EditText editComment = findViewById(R.id.edit_comment);
            if(TextUtils.isEmpty(editComment.getText())) {
                setResult(RESULT_CANCELED, result);
            }
            else{
                Incident incident;
                String comment = editComment.getText().toString();
                switch(type) {
                    case INCIDENT_TEMPERATURE: incident = new Temperature(reportId, type, comment); break;
                    case INCIDENT_RAIN: incident = new Rain(reportId, type, comment); break;
                    case INCIDENT_HAIL: incident = new Hail(reportId, type, comment); break;
                    case INCIDENT_FOG: incident = new Fog(reportId, type, comment); break;
                    case INCIDENT_CLOUD: incident = new Cloud(reportId, type, comment); break;
                    case INCIDENT_STORM: incident = new Storm(reportId, type, comment); break;
                    case INCIDENT_WIND: incident = new Wind(reportId, type, comment); break;
                    case INCIDENT_CURRENT: incident = new Current(reportId, type, comment); break;
                    case INCIDENT_TRANSPARENCY: incident = new Transparency(reportId, type, comment); break;
                    default: incident = new Other(reportId, type, comment);//INCIDENT_OTHER
                }
                Toast.makeText(this, "rep: " + incident.reportId + " inc: " +
                        incident.incidentId + " com: "+ incident.comment, Toast.LENGTH_SHORT).show();
                //put extra*/
                result.putExtra(ReportActivity.EXTRA_INCIDENT, incident);
                setResult(RESULT_OK, result);
            }
            finish();
        });
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