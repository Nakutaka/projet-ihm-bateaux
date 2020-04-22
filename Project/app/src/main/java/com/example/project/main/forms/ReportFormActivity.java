package com.example.project.main.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.data.model.IncidentWithInfo;
import com.example.project.main.MainActivity;
import com.example.project.types.ITypeIncident;
import com.example.project.types.ITypeParam;
import com.example.project.main.fragments.ReportFormFragment;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportFormActivity extends AppCompatActivity implements IButtonClickedListenerIncident, ITypeIncident {

    public static final int NEW_INCIDENT_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_INCIDENT = "com.example.project.main.activities.report.INCIDENT";
    private EditText mEditCommentView;

    private long start;
    private List<IncidentWithInfo> incidents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        start = getIntent().getIntExtra(ITypeParam.MAP_ACTIVITY_START, -1);
        incidents = new ArrayList<>();

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
                result.putParcelableArrayListExtra(MainActivity.EXTRA_INCIDENT_LIST,
                        (ArrayList<IncidentWithInfo>) incidents);
                setResult(RESULT_OK, result);
            }
            finish();
        });
    }

    private int alreadyExistingIncident(int type) {
        for (int i = 0; i< incidents.size(); i++) {
            if(incidents.get(i).getInfo().getType() == type) return i;
        }
        return -1;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_INCIDENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            IncidentWithInfo incident = data.getParcelableExtra(EXTRA_INCIDENT);
            if(incident == null) {
                Toast.makeText(
                        getApplicationContext(),
                        "Null incident",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int i;
            if((i = alreadyExistingIncident(incident.getInfo().getType())) != -1) {
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

    private void startIncidentActivity(int globalType, int incidentType, int icon) {
        Intent intent = new Intent(getApplicationContext(), IncidentFormActivity.class);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_GLOBAL_TYPE, globalType);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, incidentType);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_ICON, icon);
        startActivityForResult(intent, NEW_INCIDENT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onButtonTempClicked(View button) { startIncidentActivity(INCIDENT_UNIT,
            TEMPERATURE, R.drawable.ic_temperature); }

    @Override
    public void onButtonRainClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            RAIN, R.drawable.ic_rain); }

    @Override
    public void onButtonHailClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            HAIL, R.drawable.ic_hail); }

    @Override
    public void onButtonFogClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            FOG, R.drawable.ic_fog); }

    @Override
    public void onButtonCloudClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            CLOUD, R.drawable.ic_cloud); }

    @Override
    public void onButtonStormClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            STORM, R.drawable.ic_storm); }

    @Override
    public void onButtonWindClicked(View button) { startIncidentActivity(INCIDENT_UNIT,
            WIND, R.drawable.ic_wind); }

    @Override
    public void onButtonCurrentClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            CURRENT, R.drawable.ic_current); }

    @Override
    public void onButtonTransparencyClicked(View button) { startIncidentActivity(INCIDENT_UNIT,
            TRANSPARENCY, R.drawable.ic_transparency); }

    @Override
    public void onButtonOtherClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            OTHER, R.drawable.ic_other); }
}
