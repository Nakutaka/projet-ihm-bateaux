package com.example.project.main.fragments.incidents;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WindFragment extends Fragment {

    private IIncidentActivityFragment mCallback;
    public WindFragment() {}
    public WindFragment(IIncidentActivityFragment activity){
        mCallback = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wind,container,false);

        NumberPicker np = rootView.findViewById(R.id.number_picker_wind);
        EditText editText = rootView.findViewById(R.id.edit_wind_value);
        Spinner spinner = rootView.findViewById(R.id.spinner_wind);

        np.setMinValue(0);
        np.setMaxValue(12);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                update(Integer.toString(np.getValue()),"(Beaufort scale)");
            }
        });

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