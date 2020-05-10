package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Fog extends Info {
    public Fog() {
        super(ITypeIncident.NAME_FOG, ITypeIncident.ICON_FOG);
    }
}