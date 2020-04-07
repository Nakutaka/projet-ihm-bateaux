package com.example.projet_notifs_test;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class InitChannel extends Application {

    public static final String CHANNEL_ID = "weather";
    private static final String CHANNEL_NAME = "weather events channel";
    private static final String CHANNEL_DESCRIPTION = "channel for notifications on new weather events in the app";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        //API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = CHANNEL_NAME;//getString(R.string.channel_name);
            String description = CHANNEL_DESCRIPTION;//getString(getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;//IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            //unchangeable after, "final"
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            //verification
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
