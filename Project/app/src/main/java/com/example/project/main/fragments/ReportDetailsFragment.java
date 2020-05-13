package com.example.project.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.main.MainActivity;
import com.example.project.main.ReportAdapter;
import com.example.project.main.TwitterActivity;
import com.example.project.model.Tweet;
import com.example.project.model.unused.Date;
import com.example.project.model.weather.WeatherReport;
import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.google.gson.Gson;

import java.util.Calendar;

public class ReportDetailsFragment extends Fragment {
    private Gson gson = new Gson();


    public ReportDetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SettingsViewModel vm_settings = new SettingsViewModel(getActivity().getApplication());
        View rootView = inflater.inflate(R.layout.fragment_report_details, container,false);
        ImageButton btn_tweet = rootView.findViewById(R.id.btn_tweet);
        if (vm_settings.getCurrentSettings().hasTwitterAccount())
            btn_tweet.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.btn_close_details).setOnClickListener(view -> getParentFragmentManager().popBackStack());
        rootView.findViewById(R.id.coverViewDetails).setOnClickListener(view -> getParentFragmentManager().popBackStack());
        WeatherReport report = null;
        Bundle bundle = getArguments();
        Date dateOfReport = new Date(Calendar.getInstance());
        if (bundle != null) {
            report = gson.fromJson(bundle.getString(MainActivity.EXTRA_REPORT), WeatherReport.class);
            WeatherReport finalReport = report;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(finalReport.getReport().getTime());
            dateOfReport = new Date(calendar);
            Date finalDateOfReport = dateOfReport;
            rootView.findViewById(R.id.btn_tweet).setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), TwitterActivity.class);
                appendProgressCircle(View.VISIBLE);
                startActivityForResult(TwitterActivity.tweetIntentBuilder(intent, gson.toJson(prepareTweet(finalReport, finalDateOfReport)), vm_settings.getTwitterToken()), TwitterActivity.TWEET_REQUEST);
            });
        }
        addDetails(report, rootView, dateOfReport);
        return rootView;
    }

    private void addDetails(WeatherReport report, View view, Date date) {
        TextView text = view.findViewById(R.id.txt_date_report);
        text.setText(String.format("Posted %s ago.", getDateField(date)));
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


    private Tweet prepareTweet(WeatherReport wp, Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append("Report posted on ").append(date.getDateOnly()).append("\n");
        for (MinIncident i : wp.getMinIncidentList()) {
            sb.append(i.getInfo().getName()).append(" ").append(i.getComment());
            sb.append("\n");
        }
        for (BasicIncident b : wp.getBasicIncidentList()) {
            sb.append(b.getInfo().getName()).append(" ").append(b.getComment());
            sb.append("\n");
        }
        for (MeasuredIncident m : wp.getMeasuredIncidentList()) {
            sb.append(m.getInfo().getName()).append(" ").append(m.getComment());
            if (!m.getValue().equals("value") && !m.getUnit().equals("unit"))
                sb.append("value").append(m.getValue()).append(" ").append(m.getUnit());
            sb.append("\n");
        }
        return new Tweet(sb.toString(), "#SeaReports", "#Polytech");
    }

    private void appendProgressCircle(int state) {
        requireActivity().findViewById(R.id.loadingPanel).setVisibility(state);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        appendProgressCircle(View.GONE);

    }

}