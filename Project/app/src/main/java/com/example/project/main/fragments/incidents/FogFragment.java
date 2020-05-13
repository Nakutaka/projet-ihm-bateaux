package com.example.project.main.fragments.incidents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.types.ITypeIncident;

public class FogFragment extends Fragment {

    private IIncidentActivityFragment mCallback;
    public FogFragment() {}
    public FogFragment(IIncidentActivityFragment activity){
        mCallback = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic_incident, container,false);
        int icon = R.drawable.ic_fog;
        ((ImageView)rootView.findViewById(R.id.img_level1_1)).setImageResource(icon);
        ((ImageView)rootView.findViewById(R.id.img_level2_1)).setImageResource(icon);
        ((ImageView)rootView.findViewById(R.id.img_level2_2)).setImageResource(icon);
        ((ImageView)rootView.findViewById(R.id.img_level3_1)).setImageResource(icon);
        ((ImageView)rootView.findViewById(R.id.img_level3_2)).setImageResource(icon);
        ((ImageView)rootView.findViewById(R.id.img_level3_3)).setImageResource(icon);

        rootView.findViewById(R.id.fab_level_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(ITypeIncident.LEVEL_ONE);
            }
        });

        rootView.findViewById(R.id.fab_level_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(ITypeIncident.LEVEL_TWO);
            }
        });

        rootView.findViewById(R.id.fab_level_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(ITypeIncident.LEVEL_THREE);
            }
        });
        return rootView;


    }

    private void update(String type){
        mCallback.onIncidentUpdated(type, null);
    }
}