package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Storm extends Info {
    public Storm() {
        super(ITypeIncident.NAME_STORM, ITypeIncident.ICON_STORM);
    }
}