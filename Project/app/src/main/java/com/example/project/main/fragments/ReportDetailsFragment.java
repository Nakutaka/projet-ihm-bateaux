package com.example.project.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.data.model.WeatherReport;
import com.example.project.main.MainActivity;
import com.example.project.main.ReportAdapter;
import com.google.gson.Gson;

public class ReportDetailsFragment extends Fragment {
    private Gson gson = new Gson();


    public ReportDetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_report_details, container,false);
        rootView.findViewById(R.id.btn_close_details).setOnClickListener(view -> getParentFragmentManager().popBackStack());
        rootView.findViewById(R.id.coverViewDetails).setOnClickListener(view -> getParentFragmentManager().popBackStack());
        WeatherReport report = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            report = gson.fromJson(bundle.getString(MainActivity.EXTRA_REPORT), WeatherReport.class);
        }
        addDetails(report, rootView);
        return rootView;
    }

    private void addDetails(WeatherReport report, View view) {
        ReportAdapter reportAdapter = new ReportAdapter(report.getIncidentList());
        RecyclerView rvReport = view.findViewById(R.id.rv_report);
        rvReport.setAdapter(reportAdapter);
        rvReport.setLayoutManager(new LinearLayoutManager(getContext()));

    }


}