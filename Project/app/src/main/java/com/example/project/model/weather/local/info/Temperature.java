package com.example.project.data.model.weather.local.info;

import com.example.project.data.model.weather.local.Info;
import com.example.project.types.ITypeIncident;

public class Temperature extends Info {
    public Temperature() {
        super(ITypeIncident.NAME_TEMPERATURE, ITypeIncident.ICON_TEMPERATURE);
    }
}
        /* String imgName = "car" + carType + num%10;
        //String ressourceName = String.format(Locale.getDefault(),"car%02d",car.getNumImage());
        int id = getResources().getIdentifier(imgName, "drawable", getPackageName());
        img.setImageResource(id);*/
