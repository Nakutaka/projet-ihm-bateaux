package com.example.project.main.fragments.incidents;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import java.util.ArrayList;

public class TemperatureFragment extends Fragment {

    private IIncidentActivityFragment mCallback;
    public TemperatureFragment() {}
    public TemperatureFragment(IIncidentActivityFragment activity){
        mCallback = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_temperature,container,false);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        EditText editText = rootView.findViewById(R.id.edit_temperature_value);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update(editText.getText().toString(),null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update(null,spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        return rootView;
    }

    private void update(String value, String unit){
        if (mCallback!=null) mCallback.onIncidentUpdated(value, unit);
    }
}