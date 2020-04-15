package com.example.projet.activities;

import android.os.Bundle;

import com.example.projet.R;
import com.example.projet.fragments.ITypeFragment;
import com.example.projet.fragments.incidents.CloudFragment;
import com.example.projet.fragments.incidents.CurrentFragment;
import com.example.projet.fragments.incidents.FogFragment;
import com.example.projet.fragments.incidents.HailFragment;
import com.example.projet.fragments.incidents.OtherFragment;
import com.example.projet.fragments.incidents.RainFragment;
import com.example.projet.fragments.incidents.StormFragment;
import com.example.projet.fragments.incidents.TempFragment;
import com.example.projet.fragments.incidents.TransparencyFragment;
import com.example.projet.fragments.incidents.WindFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class IncidentActivity extends AppCompatActivity implements ITypeFragment {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
        int type = getIntent().getIntExtra(ACTIVITY_INCIDENT_TYPE, 0);
        switch(type) {
            case INCIDENT_TEMP: displayIncidentFragment(new TempFragment()); break;
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