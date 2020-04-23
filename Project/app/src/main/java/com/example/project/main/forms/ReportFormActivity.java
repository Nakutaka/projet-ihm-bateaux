package com.example.project.main.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.data.model.incident.BasicIncident;
import com.example.project.data.model.incident.MeasuredIncident;
import com.example.project.data.model.incident.MinIncident;
import com.example.project.main.MainActivity;
import com.example.project.types.ITypeIncident;
import com.example.project.types.ITypeParam;
import com.example.project.main.fragments.ReportFormFragment;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportFormActivity extends AppCompatActivity implements IButtonClickedListenerIncident, ITypeIncident {

    public static final int NEW_INCIDENT_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_INCIDENT_MIN = "com.example.project.main.activities.report.INCIDENT_MIN";
    public static final String EXTRA_INCIDENT_BASIC = "com.example.project.main.activities.report.INCIDENT_BASIC";
    public static final String EXTRA_INCIDENT_MEASURED = "com.example.project.main.activities.report.INCIDENT_MEASURED";
    private EditText mEditCommentView;

    //private List<Incident> incidents;
    private List<MinIncident> minList;
    private List<BasicIncident> basicList;
    private List<MeasuredIncident> measuredList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        minList = new ArrayList<>();
        basicList = new ArrayList<>();
        measuredList = new ArrayList<>();

        ReportFormFragment reportFormFragment = new ReportFormFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_report,
                reportFormFragment).commit();

        findViewById(R.id.button_send).setOnClickListener(view -> {
            //mEditCommentView = findViewById(R.id.edit_word);
            //general comment
            //+ GPS chosen coordinates
            Intent result = new Intent();
            result.putParcelableArrayListExtra(MainActivity.EXTRA_INCIDENT_MIN_LIST,
                (ArrayList<MinIncident>) minList);
            result.putParcelableArrayListExtra(MainActivity.EXTRA_INCIDENT_BASIC_LIST,
                (ArrayList<BasicIncident>) basicList);
            result.putParcelableArrayListExtra(MainActivity.EXTRA_INCIDENT_MEASURED_LIST,
                (ArrayList<MeasuredIncident>) measuredList);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void removePossibleExistingIncident(String name) {
        for (int i = 0; i < minList.size(); i++) {
            if(minList.get(i).getInfo().getName().equals(name)) {
                minList.remove(i);
                return;
            }
        }
        for (int i = 0; i < basicList.size(); i++) {
            if(basicList.get(i).getInfo().getName().equals(name)) {
                basicList.remove(i);
                return;
            }
        }
        for (int i = 0; i < measuredList.size(); i++) {
            if (measuredList.get(i).getInfo().getName().equals(name)) {
                measuredList.remove(i);
                return;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_INCIDENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                MinIncident min = extras.getParcelable(EXTRA_INCIDENT_MIN);
                if(min!=null) {
                    removePossibleExistingIncident(min.getInfo().getName());
                    minList.add(min);
                    Toast.makeText(getApplicationContext(),
                            min.getInfo().getName() + " - " + min.getComment(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                BasicIncident basic = extras.getParcelable(EXTRA_INCIDENT_BASIC);
                if(basic!=null) {
                    removePossibleExistingIncident(basic.getInfo().getName());
                    basicList.add(basic);
                    Toast.makeText(getApplicationContext(),
                            basic.getInfo().getName() + " - " + basic.getLevel() + " - " + basic.getComment(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                MeasuredIncident measured = extras.getParcelable(EXTRA_INCIDENT_MEASURED);
                if(measured!=null) {
                    removePossibleExistingIncident(measured.getInfo().getName());
                    measuredList.add(measured);
                    Toast.makeText(getApplicationContext(),
                            measured.getInfo().getName() + " - " + measured.getValue()
                                    + " - " + measured.getUnit() + " - " + measured.getComment(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(getApplicationContext(),"No changes", Toast.LENGTH_SHORT).show();
    }

    private void startIncidentActivity(int globalType, int incidentType, int icon) {
        Intent intent = new Intent(getApplicationContext(), IncidentFormActivity.class);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_GLOBAL_TYPE, globalType);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, incidentType);
        intent.putExtra(ITypeParam.REPORT_ACTIVITY_ICON, icon);
        startActivityForResult(intent, NEW_INCIDENT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onButtonTempClicked(View button) { startIncidentActivity(INCIDENT_MEASURED,
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
    public void onButtonWindClicked(View button) { startIncidentActivity(INCIDENT_MEASURED,
            WIND, R.drawable.ic_wind); }

    @Override
    public void onButtonCurrentClicked(View button) { startIncidentActivity(INCIDENT_BASIC,
            CURRENT, R.drawable.ic_current); }

    @Override
    public void onButtonTransparencyClicked(View button) { startIncidentActivity(INCIDENT_MEASURED,
            TRANSPARENCY, R.drawable.ic_transparency); }

    @Override
    public void onButtonOtherClicked(View button) { startIncidentActivity(INCIDENT_MIN,
            OTHER, R.drawable.ic_other); }
}
