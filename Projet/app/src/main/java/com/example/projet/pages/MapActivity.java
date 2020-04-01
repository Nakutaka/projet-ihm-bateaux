package com.example.projet.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projet.R;
import com.example.projet.core.ISignalement;
import com.example.projet.pages.fragments.InfoBox;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        displayInfo(null);
        findViewById(R.id.bouton_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignalementActivity.class);
                startActivity(intent);
            }
        });
    }

    void displayInfo(ISignalement signalement){
        InfoBox infoBox = new InfoBox();
        getSupportFragmentManager().beginTransaction().replace(R.id.infoBox,infoBox).commit();
    }
}
