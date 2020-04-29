package com.example.project.control;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.project.data.model.SettingsModel;

public class SettingsViewModel extends ViewModel {
//DATA

    SettingsModel settingsModel;
    
    public SettingsViewModel(){
        super();
        settingsModel = new SettingsModel();
        init();
    }

    private void init() {
    }
}
