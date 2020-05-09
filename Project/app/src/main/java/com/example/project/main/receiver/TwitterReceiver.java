package com.example.project.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.main.worker.TwitterWorker;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterReceiver extends BroadcastReceiver {
    Gson gson = new Gson();
        public TwitterReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
                Toast.makeText(context, "Tweet sent!", Toast.LENGTH_SHORT).show();
            } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
                Intent retryLater = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
                retryLater.setExtrasClassLoader(TwitterAuthToken.class.getClassLoader());
                if (retryLater != null)
                    createWork(context, retryLater);
                Toast.makeText(context, "No signal. The report will be sent later.", Toast.LENGTH_LONG).show();
            } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
                Toast.makeText(context, "Tweet aborted.", Toast.LENGTH_SHORT).show();
            }
        }

    private void createWork(Context context, Intent intent) {
        //Using sharedPref because of a bug to retrieve EXTRA_USER_TOKEN from intent
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsViewModel.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        TwitterSession session = gson.fromJson(sharedPreferences.getString(context.getString(R.string.key_twitter), null), TwitterSession.class);
        final TwitterAuthToken token = session.getAuthToken();
        Data tweetData = new Data.Builder()
                .putString(TwitterWorker.EXTRA_TWEET_TEXT, intent.getStringExtra(TwitterWorker.EXTRA_TWEET_TEXT))
                .putString(TwitterWorker.EXTRA_USER_TOKEN, token.token)
                .putString(TwitterWorker.EXTRA_USER_SECRET, token.secret)
                .build();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest uploadTweet = new OneTimeWorkRequest.Builder(TwitterWorker.class)
                .setInputData(tweetData)
                .setConstraints(constraints)
                .build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(uploadTweet);
    }


}
