package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Rain extends Info {
    public Rain() {
        super(ITypeIncident.NAME_RAIN, ITypeIncident.ICON_RAIN);
    }
}