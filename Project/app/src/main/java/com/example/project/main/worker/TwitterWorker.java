package com.example.project.main.worker;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import static com.twitter.sdk.android.tweetcomposer.TweetUploadService.EXTRA_TWEET_ID;
import static com.twitter.sdk.android.tweetcomposer.TweetUploadService.UPLOAD_FAILURE;
import static com.twitter.sdk.android.tweetcomposer.TweetUploadService.UPLOAD_SUCCESS;


public class TwitterWorker extends Worker {
    public static final String EXTRA_USER_TOKEN = "TOKEN";
    public static final String EXTRA_USER_SECRET = "SECRET";
    public static final String EXTRA_TWEET_TEXT = "EXTRA_TWEET_TEXT";
    public static final String EXTRA_HASHTAGS = "EXTRA_HASHTAGS";
    private static final int PLACEHOLDER_ID = -1;
    private static final String PLACEHOLDER_SCREEN_NAME = "";
    private Gson gson = new Gson();
    public TwitterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Twitter.initialize(getApplicationContext());
        final TwitterAuthToken token = new TwitterAuthToken(getInputData().getString(EXTRA_USER_TOKEN), getInputData().getString(EXTRA_USER_SECRET));
        TwitterSession session = new TwitterSession(token, PLACEHOLDER_ID, PLACEHOLDER_SCREEN_NAME);
        final TwitterApiClient client = TwitterCore.getInstance().getApiClient(session);
        client.getStatusesService().update(getInputData().getString(EXTRA_TWEET_TEXT), null, null, null, null, null, null, true, null)
                .enqueue(
                        new Callback<com.twitter.sdk.android.core.models.Tweet>() {
                            @Override
                            public void success(com.twitter.sdk.android.core.Result<com.twitter.sdk.android.core.models.Tweet> result) {
                                sendSuccessBroadcast(result.data.getId());
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                sendFailureBroadcast();
                            }
                        });

        return Result.success();
    }

    private void sendSuccessBroadcast(long tweetId) {
        final Intent intent = new Intent(UPLOAD_SUCCESS);
        intent.putExtra(EXTRA_TWEET_ID, tweetId);
        intent.setPackage(getApplicationContext().getPackageName());
        getApplicationContext().sendBroadcast(intent);
    }

    private void sendFailureBroadcast() {
        final Intent intent = new Intent(UPLOAD_FAILURE);
        getApplicationContext().sendBroadcast(intent);
    }

}
