package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Cloud extends Info {
    public Cloud() {
        super(ITypeIncident.NAME_CLOUD, ITypeIncident.ICON_CLOUD);
    }
}