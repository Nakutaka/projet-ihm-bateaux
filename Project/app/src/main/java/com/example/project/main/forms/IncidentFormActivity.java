package com.example.project.main.forms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.main.fragments.incidents.IIncidentActivityFragment;
import com.example.project.model.weather.local.Incident;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class IncidentFormActivity extends AppCompatActivity implements ITypeIncident, IIncidentActivityFragment {

    private int globalType;
    private int type;
    private int icon;
    private String comment;

    private String data;
    private String unit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_form);

        Toolbar mytoolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        Intent intent = getIntent();
        globalType = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_GLOBAL_TYPE, 0);
        type = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_INCIDENT_TYPE, 0);
        icon = intent.getIntExtra(ITypeParam.REPORT_ACTIVITY_ICON, 0);
        String title = intent.getStringExtra(ITypeParam.REPORT_ACTIVITY_TITLE);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayFragment(globalType, type);

        if (savedInstanceState!=null){
            data = savedInstanceState.getString("VALUE");
            unit = savedInstanceState.getString("UNIT");
        }

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void toastyIncident(String type, int id, String com) {
        Toast.makeText(
                getApplicationContext(), type + " - " + id + " - " + com,
                Toast.LENGTH_SHORT).show();
    }

    //TODO value
    Incident newIncident() {
        IncidentFactory_classic factory = new IncidentFactory_classic();
        if (data==null) return null;
        return factory.getIncident(globalType, type, data, unit, comment);
    }

    void displayFragment(int globalType, int type) {
        // Prevent recreation
        //if (getSupportFragmentManager().findFragmentById(R.id.frame_layout_incident)!=null)return;
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
            case TEMPERATURE: displayIncidentFragment(new TemperatureFragment(this)); break;
            case RAIN: displayIncidentFragment(new RainFragment(this)); break;
            case HAIL: displayIncidentFragment(new HailFragment(this)); break;
            case FOG: displayIncidentFragment(new FogFragment(this)); break;
            case CLOUD: displayIncidentFragment(new CloudFragment(this)); break;
            case STORM: displayIncidentFragment(new StormFragment(this)); break;
            case WIND: displayIncidentFragment(new WindFragment(this)); break;
            case CURRENT: displayIncidentFragment(new CurrentFragment(this)); break;
            case TRANSPARENCY: displayIncidentFragment(new TransparencyFragment(this)); break;
            default: displayIncidentFragment(new OtherFragment(this));//INCIDENT_OTHER
        }
    }

    void displayIncidentFragment(Fragment incidentFragment) {
        Bundle args = new Bundle();
        incidentFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_incident,
                incidentFragment).commit();
    }

    @Override
    public void onIncidentUpdated(String data, String unit) {

        if(data!=null)this.data = data;
        if(unit!=null)this.unit = unit;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("VALUE",data);
        outState.putString("UNIT",unit);
    }
}