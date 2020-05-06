package com.example.project.main.fragments;

public interface IGPSActivity {
    String GPS_LOG_TOKEN = "GPS-LOGS";
    int MY_PERMISSION_ACCESS_LOCATION = 10;
    void focus(boolean resetZoom); // center the map on the last GPS position
}
