package com.example.project.model.weather.local.info;

import com.example.project.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Hail extends Info {
    public Hail() {
        super(ITypeIncident.NAME_HAIL, ITypeIncident.ICON_HAIL);
    }
}