package com.example.project.types;

public interface ITypeIncident {
    int INCIDENT_MIN = 0;
    int INCIDENT_BASIC = 1;
    int INCIDENT_MEASURED = 2;

    int TEMPERATURE = 10;
    int RAIN = 11;
    int HAIL = 12;
    int FOG = 13;
    int CLOUD = 14;
    int STORM = 15;
    int WIND = 16;
    int CURRENT = 17;
    int TRANSPARENCY = 18;
    int OTHER = 19;

    String LEVEL_ONE = "low";//1
    String LEVEL_TWO = "medium";//2
    String LEVEL_THREE = "high";//3

    String NAME_TEMPERATURE = "Temperature";
    String NAME_RAIN = "Rain";
    String NAME_HAIL = "Hail";
    String NAME_FOG = "Fog";
    String NAME_CLOUD = "Cloud";
    String NAME_STORM = "Storm";
    String NAME_WIND = "Wind";
    String NAME_CURRENT = "Current";
    String NAME_TRANSPARENCY = "Transparency";
    String NAME_OTHER = "Other";

    String ICON_TEMPERATURE = "ic_temperature";
    String ICON_RAIN = "ic_rain";
    String ICON_HAIL = "ic_hail";
    String ICON_FOG = "ic_fog";
    String ICON_CLOUD = "ic_cloud";
    String ICON_STORM = "ic_storm";
    String ICON_WIND = "ic_wind";
    String ICON_CURRENT = "ic_current";
    String ICON_TRANSPARENCY = "ic_transparency";
    String ICON_OTHER = "ic_other";
    /* String imgName = "car" + carType + num%10;
        //String ressourceName = String.format(Locale.getDefault(),"car%02d",car.getNumImage());
        int id = getResources().getIdentifier(imgName, "drawable", getPackageName());
        img.setImageResource(id);*/
}
