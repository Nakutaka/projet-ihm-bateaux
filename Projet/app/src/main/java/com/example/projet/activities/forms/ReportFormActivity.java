package com.example.projet.activities.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.R;
import com.example.projet.activities.MainActivity;
import com.example.projet.database.models.Incident;
import com.example.projet.types.ITypeIncident;
import com.example.projet.types.ITypeParam;
import com.example.projet.fragments.ReportFormFragment;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportFormActivity extends AppCompatActivity implements IButtonClickedListenerIncident, ITypeIncident {

    public static final int NEW_INCIDENT_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_INCIDENT = "com.example.projet.activities.report.INCIDENT";
    private EditText mEditCommentView;

    private long start;
    private List<Incident> incidents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        start = getIntent().getIntExtra(ITypeParam.MAP_ACTIVITY_START, -1);
        incidents = new ArrayList<Incident>();

        ReportFormFragment reportFormFragment = new ReportFormFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_report,
                reportFormFragment).commit();

        findViewById(R.id.button_send).setOnClickListener(view -> {
            //mEditCommentView = findViewById(R.id.edit_word);
            //general comment
            Intent result = new Intent();//getIntent();//new Intent();
            if(incidents.isEmpty()){
                setResult(RESULT_CANCELED, result);
            }
            else{
                //result.putParcelableArrayListExtra(MapActivity.EXTRA_INCIDENT_LIST, (ArrayList<Incident>) incidents);;
                incidents.forEach(i -> {
                    switch(i.incidentId) {
                        case INCIDENT_CLOUD: result.putExtra(MainActivity.EXTRA_INCIDENT_CLOUD, i); break;
                        case INCIDENT_CURRENT: result.putExtra(MainActivity.EXTRA_INCIDENT_CURRENT, i); break;
                        case INCIDENT_FOG: result.putExtra(MainActivity.EXTRA_INCIDENT_FOG, i); break;
                        case INCIDENT_HAIL: result.putExtra(MainActivity.EXTRA_INCIDENT_HAIL, i); break;
                        case INCIDENT_RAIN: result.putExtra(MainActivity.EXTRA_INCIDENT_RAIN, i); break;
                        case INCIDENT_STORM: result.putExtra(MainActivity.EXTRA_INCIDENT_STORM, i); break;
                        case INCIDENT_TEMPERATURE: result.putExtra(MainActivity.EXTRA_INCIDENT_TEMPERATURE, i); break;
                        case INCIDENT_TRANSPARENCY: result.putExtra(MainActivity.EXTRA_INCIDENT_TRANSPARENCY, i); break;
                        case INCIDENT_WIND: result.putExtra(MainActivity.EXTRA_INCIDENT_WIND, i); break;
                        default: result.putExtra(MainActivity.EXTRA_INCIDENT_OTHER, i);
                    }
                });
                setResult(RESULT_OK, result);
            }
            finish();
        });
    }

    private int alreadyExistingIncident(int type) {
        for (int i=0; i<incidents.size(); i++) {
            if(incidents.get(i).incidentId == type) return i;
        }
        return -1;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_INCIDENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Incident incident = data.getParcelableExtra(EXTRA_INCIDENT);
            if(incident == null) {
                Toast.makeText(
                        getApplicationContext(),
                        "Null incident",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int i;
            if((i = alreadyExistingIncident(incident.incidentId)) != -1) {
                incidents.set(i, incident);
            }
            else incidents.add(incident);
        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    "No changes",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startIncidentActivity(int incidentType) {
        Intent intent = new Intent(getApplicationContext(), IncidentFormActivity.class);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, incidentType);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_START, start);
        startActivityForResult(intent, NEW_INCIDENT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onButtonTempClicked(View button) { startIncidentActivity(INCIDENT_TEMPERATURE); }

    @Override
    public void onButtonRainClicked(View button) { startIncidentActivity(INCIDENT_RAIN); }

    @Override
    public void onButtonHailClicked(View button) { startIncidentActivity(INCIDENT_HAIL); }

    @Override
    public void onButtonFogClicked(View button) { startIncidentActivity(INCIDENT_FOG); }

    @Override
    public void onButtonCloudClicked(View button) { startIncidentActivity(INCIDENT_CLOUD); }

    @Override
    public void onButtonStormClicked(View button) { startIncidentActivity(INCIDENT_STORM); }

    @Override
    public void onButtonWindClicked(View button) { startIncidentActivity(INCIDENT_WIND); }

    @Override
    public void onButtonCurrentClicked(View button) { startIncidentActivity(INCIDENT_CURRENT); }

    @Override
    public void onButtonTransparencyClicked(View button) { startIncidentActivity(INCIDENT_TRANSPARENCY); }

    @Override
    public void onButtonOtherClicked(View button) { startIncidentActivity(INCIDENT_OTHER); }
}
