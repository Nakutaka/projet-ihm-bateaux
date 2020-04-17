package com.example.projet.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.projet.activities.IButtonClickedListenerIncident;
import com.example.projet.R;

public class ReportFragment extends Fragment implements View.OnClickListener {
    private IButtonClickedListenerIncident mCallBack;

    public ReportFragment() {}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallBack = (IButtonClickedListenerIncident) getActivity();
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
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
        if(btn.getId() == R.id.fab_temp) mCallBack.onButtonTempClicked(btn);
        else if(btn.getId() == R.id.fab_rain) mCallBack.onButtonRainClicked(btn);
        else if(btn.getId() == R.id.fab_hail) mCallBack.onButtonHailClicked(btn);
        else if(btn.getId() == R.id.fab_fog) mCallBack.onButtonFogClicked(btn);
        else if(btn.getId() == R.id.fab_cloud) mCallBack.onButtonCloudClicked(btn);
        else if(btn.getId() == R.id.fab_storm) mCallBack.onButtonStormClicked(btn);
        else if(btn.getId() == R.id.fab_wind) mCallBack.onButtonWindClicked(btn);
        else if(btn.getId() == R.id.fab_current) mCallBack.onButtonCurrentClicked(btn);
        else if(btn.getId() == R.id.fab_transparency) mCallBack.onButtonTransparencyClicked(btn);
        else if(btn.getId() == R.id.fab_other) mCallBack.onButtonOtherClicked(btn);
    }
}