package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Current extends Info {
    public Current() {
        super(ITypeIncident.NAME_CURRENT, ITypeIncident.ICON_CURRENT);
    }
}