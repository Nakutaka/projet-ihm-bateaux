package com.example.projet_notifs_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static com.example.projet_notifs_test.InitChannel.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private int NOTIFICATION_ID = 0;
    private int DURATION_MS = 4000;//4sec

    private View.OnClickListener basicOnClickListener = new View.OnClickListener(){
        @Override public void onClick(View v){
            sendMockNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonNotify).setOnClickListener(basicOnClickListener);
    }

    private void sendMockNotification() {
        Intent intent = new Intent(this, MainActivity.class);//AlertDetails.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New event")
                .setContentText("Rain: high")
                .setPriority(NotificationCompat.PRIORITY_MAX)//PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)//launch activity specified
                .setAutoCancel(true)//dismiss notification
                .setTimeoutAfter(DURATION_MS);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID++, builder.build());
    }
}