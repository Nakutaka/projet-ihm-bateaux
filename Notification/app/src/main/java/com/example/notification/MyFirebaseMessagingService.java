package com.example.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CANAL ="MyNotifCanal" ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String myMessage=remoteMessage.getNotification().getBody();
        Log.d( "FirebaseMessage","Vous venez de recevoire une notifiaction :"+myMessage);

        //action
        //rediriger vers une activity (NotifActivity)
        Intent intent = new Intent(getApplicationContext(),NotifActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, 0);

        //créer une notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CANAL);
        notificationBuilder.setContentTitle("Ma notif !!");
        notificationBuilder.setContentText(myMessage);

        //ajouter une action
        notificationBuilder.setContentIntent(pendingIntent);

        //ajouter une icone
        notificationBuilder.setSmallIcon(R.drawable.bell);

        //envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            String channelId = getString(R.string.notification_channel_id);
            String channelTitle = getString(R.string.notification_channel_title);
            String channelDescription = getString(R.string.notification_channel_desc);
            NotificationChannel channel = new NotificationChannel(channelId,channelTitle,NotificationManager.IMPORTANCE_DEFAULT );
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);

        }


        notificationManager.notify(1,notificationBuilder.build());


    }
}
