package com.example.project.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.project.main.forms.IButtonClickedListenerIncident;
import com.example.project.R;

public class ReportFormFragment extends Fragment implements View.OnClickListener {
    private IButtonClickedListenerIncident mCallBack;

    public ReportFormFragment() {}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallBack = (IButtonClickedListenerIncident) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_form, container, false);
        rootView.findViewById(R.id.fab_temp).setOnClickListener(this);
        rootView.findViewById(R.id.fab_rain).setOnClickListener(this);
        rootView.findViewById(R.id.fab_hail).setOnClickListener(this);
        rootView.findViewById(R.id.fab_fog).setOnClickListener(this);
        rootView.findViewById(R.id.fab_cloud).setOnClickListener(this);
        rootView.findViewById(R.id.fab_storm).setOnClickListener(this);
        rootView.findViewById(R.id.fab_wind).setOnClickListener(this);
        rootView.findViewById(R.id.fab_current).setOnClickListener(this);
        rootView.findViewById(R.id.fab_transparency).setOnClickListener(this);
        rootView.findViewById(R.id.fab_other).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View btn) {
        switch (btn.getId()){
            case R.id.fab_temp: mCallBack.onButtonTempClicked(btn); break;
            case R.id.fab_rain: mCallBack.onButtonRainClicked(btn); break;
            case R.id.fab_hail: mCallBack.onButtonHailClicked(btn); break;
            case R.id.fab_fog: mCallBack.onButtonFogClicked(btn); break;
            case R.id.fab_cloud: mCallBack.onButtonCloudClicked(btn); break;
            case R.id.fab_storm: mCallBack.onButtonStormClicked(btn); break;
            case R.id.fab_wind: mCallBack.onButtonWindClicked(btn); break;
            case R.id.fab_current: mCallBack.onButtonCurrentClicked(btn); break;
            case R.id.fab_transparency: mCallBack.onButtonTransparencyClicked(btn); break;
            case R.id.fab_other: mCallBack.onButtonOtherClicked(btn); break;
        }
    }
}