package com.example.project.main.forms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.data.model.Incident;
import com.example.project.main.factory.IncidentFactory_classic;
import com.example.project.types.ITypeIncident;
import com.example.project.types.ITypeParam;
import com.example.project.main.fragments.incidents.CloudFragment;
import com.example.project.main.fragments.incidents.CurrentFragment;
import com.example.project.main.fragments.incidents.FogFragment;
import com.example.project.main.fragments.incidents.HailFragment;
import com.example.project.main.fragments.incidents.OtherFragment;
import com.example.project.main.fragments.incidents.RainFragment;
import com.example.project.main.fragments.incidents.StormFragment;
import com.example.project.main.fragments.incidents.TemperatureFragment;
import com.example.project.main.fragments.incidents.TransparencyFragment;
import com.example.project.main.fragments.incidents.WindFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class IncidentFormActivity extends AppCompatActivity implements ITypeIncident {

    private int globalType;
    private int type;
    private int icon;
    private String comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_form);

        Intent intent = getIntent();
        globalType = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_GLOBAL_TYPE, 0);
        type = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, 0);
        icon = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_ICON, 0);

        displayFragment(globalType, type);

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
                comment = editComment.getText().toString();
                Incident incident = newIncident();
                if(incident != null) {
                    String param;
                    switch(globalType) {
                        case INCIDENT_MIN:
                            param = ReportFormActivity.EXTRA_INCIDENT_MIN;
                            break;
                        case INCIDENT_BASIC:
                            param = ReportFormActivity.EXTRA_INCIDENT_BASIC;
                            break;
                        case INCIDENT_MEASURED:
                            param = ReportFormActivity.EXTRA_INCIDENT_MEASURED;
                            break;
                        default:
                            setResult(RESULT_CANCELED, result);
                            finish();
                            return;
                    }
                    result.putExtra(param, incident);
                    setResult(RESULT_OK, result);
                }
            }
            finish();
        });
    }

    private void toastyIncident(String type, int id, String com) {
        Toast.makeText(
                getApplicationContext(), type + " - " + id + " - " + com,
                Toast.LENGTH_SHORT).show();
    }

    //TODO value
    Incident newIncident() {
        IncidentFactory_classic factory = new IncidentFactory_classic();
        return factory.getIncident(globalType, type, "value", "unit", comment);
    }

    void displayFragment(int globalType, int type) {
        //globalType
        //--> lambda:       buttons
        //--> scientific:   specific interface
        /*switch(globalType) {
            case INCIDENT_MIN: {
                displayIncidentFragment(new OtherFragment()); //MinimumFragment + infos
                break;
            }
            case INCIDENT_BASIC {
                displayIncidentFragment(new BasicFragment());//+ infos on incident/icon
                break;
            }
            case INCIDENT_BASIC {
                switch(type) {
                    case TEMPERATURE: displayIncidentFragment(new TemperatureFragment()); break;
                    case WIND: displayIncidentFragment(new WindFragment()); break;
                    case TRANSPARENCY: displayIncidentFragment(new TransparencyFragment()); break;
                }
            }
        }*/
        switch(type) {
            case TEMPERATURE: displayIncidentFragment(new TemperatureFragment()); break;
            case RAIN: displayIncidentFragment(new RainFragment()); break;
            case HAIL: displayIncidentFragment(new HailFragment()); break;
            case FOG: displayIncidentFragment(new FogFragment()); break;
            case CLOUD: displayIncidentFragment(new CloudFragment()); break;
            case STORM: displayIncidentFragment(new StormFragment()); break;
            case WIND: displayIncidentFragment(new WindFragment()); break;
            case CURRENT: displayIncidentFragment(new CurrentFragment()); break;
            case TRANSPARENCY: displayIncidentFragment(new TransparencyFragment()); break;
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