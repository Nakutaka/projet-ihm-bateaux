package com.example.project.main.fragments.incidents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class TemperatureFragment extends Fragment {

    public TemperatureFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_temperature,container,false);
        /*Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.select_temperature_unit, R.layout.fragment_temperature);
        //adapter.setDropDownViewResource(R.array);
        spinner.setAdapter(adapter);*/

        return rootView;
    }
}