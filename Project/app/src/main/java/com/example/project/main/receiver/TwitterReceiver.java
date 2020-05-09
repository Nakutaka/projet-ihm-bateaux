package com.example.project.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterReceiver extends BroadcastReceiver {

        public TwitterReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
                Toast.makeText(context, "Tweet sent!", Toast.LENGTH_SHORT).show();
            } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
                Toast.makeText(context, "Error while sending the tweet", Toast.LENGTH_SHORT).show();
            } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
                Toast.makeText(context, "Tweet aborted", Toast.LENGTH_SHORT).show();
            }
        }
}
