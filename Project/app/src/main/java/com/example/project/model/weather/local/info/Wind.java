package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Wind extends Info {
    public Wind() {
        super(ITypeIncident.NAME_WIND, ITypeIncident.ICON_WIND);
    }
}