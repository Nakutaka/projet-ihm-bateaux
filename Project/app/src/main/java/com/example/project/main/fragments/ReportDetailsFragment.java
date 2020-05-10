package com.example.project.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.data.model.Date;
import com.example.project.data.model.WeatherReport;
import com.example.project.main.MainActivity;
import com.example.project.main.ReportAdapter;
import com.google.gson.Gson;

import java.util.Calendar;

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
        TextView text = view.findViewById(R.id.txt_date_report);
        text.setText(String.format("Posted %s ago.", getDateField(report.getReport().getDate())));
        ReportAdapter reportAdapter = new ReportAdapter(report.getIncidentList(), getContext());
        RecyclerView rvReport = view.findViewById(R.id.rv_report);
        rvReport.setAdapter(reportAdapter);
        rvReport.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private String getDateField(Date date) {
        Date today = new Date(Calendar.getInstance());
        String result;
        if (today.day == date.day && today.month == date.month) {
            int hour = (today.hour - date.hour);
            result = hour + " " + (hour > 1 ? "hours" : "hour");
            if (hour == 0) {
                int min = (today.min - date.min);
                result = min + " " + (min > 1 ? "minutes" : "minute");
            }
        } else {
            int day = (today.day - date.day);
            result = day + " " + (day > 1 ? "days" : "day");
        }
        return result;
    }



}