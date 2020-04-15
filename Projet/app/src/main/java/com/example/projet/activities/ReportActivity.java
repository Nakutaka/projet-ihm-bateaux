package com.example.projet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.R;
import com.example.projet.database.models.Incident;
import com.example.projet.fragments.ITypeFragment;
import com.example.projet.fragments.ReportFragment;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.tileprovider.tilesource.ITileSource;

public class ReportActivity extends AppCompatActivity implements IButtonClickedListenerIncident, ITypeFragment {

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ((ImageButton)findViewById(R.id.bouton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), IncidentActivity.class);
               //intent.putExtra("key", WindReportFragment.class);
               startActivity(intent);
            }
        });
    }

    void addSignalement(IReport signalement){}*/
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditCommentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ReportFragment reportFragment = new ReportFragment();
        /*Bundle args = new Bundle();
        args.putInt(FRAGMENT_PARAMETER, entier);
        detailFragment.setArguments(args);*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_report,
                reportFragment).commit();


        //mEditCommentView = findViewById(R.id.edit_word);
        final Button button = findViewById(R.id.button_send);
        /*button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditCommentView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = mEditCommentView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });*/
    }

    /*
    @Override
    public void onButtonClearFragmentClicked(View button) {
        Toast.makeText(this, "supprimer le fragment dessous", Toast.LENGTH_LONG).show();
        ((EditText) findViewById(R.id.editText)).setText("");
        if(detailFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
        }
    }*/

    /*int valeur = getTableValue();
        Toast.makeText(this, "dans le fragment dessous " + valeur, Toast.LENGTH_LONG).show();
    detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);
        if(detailFragment == null){
        detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_PARAMETER, valeur);
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_detail, detailFragment).commit();
    }*/

    private void startIncidentActivity(int incidentType) {
        Intent intent = new Intent(getApplicationContext(), IncidentActivity.class);
        intent.putExtra(ACTIVITY_INCIDENT_TYPE, incidentType);
        startActivity(intent);
    }

    @Override
    public void onButtonTempClicked(View button) { startIncidentActivity(INCIDENT_TEMP); }

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
