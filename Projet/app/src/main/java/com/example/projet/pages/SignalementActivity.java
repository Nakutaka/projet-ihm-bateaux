package com.example.projet.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.projet.R;
import com.example.projet.core.ISignalement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignalementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signalement);
        ((ImageButton)findViewById(R.id.bouton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
               //intent.putExtra("key", WindReportFragment.class);
               startActivity(intent);
            }
        });
    }

    void addSignalement(ISignalement signalement){}
}
