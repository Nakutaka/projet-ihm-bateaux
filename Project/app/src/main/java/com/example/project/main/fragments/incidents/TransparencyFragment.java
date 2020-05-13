package com.example.project.main.fragments.incidents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class TransparencyFragment extends Fragment {

    private IIncidentActivityFragment mCallback;

    public TransparencyFragment() {}
    public TransparencyFragment(IIncidentActivityFragment activity){
        mCallback = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transparency,container,false);

        NumberPicker np = rootView.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setMaxValue(30);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                update(Integer.toString(np.getValue()),"m");
            }
        });

        return rootView;
    }

    private void update(String value, String unit){
        mCallback.onIncidentUpdated(value, unit);
    }
}