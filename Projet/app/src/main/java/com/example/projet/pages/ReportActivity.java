package com.example.projet.pages;

import android.os.Bundle;

import com.example.projet.R;
import com.example.projet.pages.fragments.report.WindReportFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        WindReportFragment windReportFragment = new WindReportFragment();
        Bundle args = new Bundle();
        windReportFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.report,windReportFragment).commit();
    }
}
