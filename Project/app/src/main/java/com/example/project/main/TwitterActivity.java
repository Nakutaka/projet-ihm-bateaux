package com.example.project.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.data.model.Tweet;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class TwitterActivity extends AppCompatActivity {
    public final static String TWITTER_ACTION = "TWEET_ACTION";
    public final static String TWITTER_SESSION = "TWITTER_SESSION";
    public final static String TWITTER_CONNECT = "TWITTER_CONNECT";
    public final static String TWEET = "TWEET";
    public final static int TWEET_REQUEST = 6;
    public static final String TWEET_INTENT = "TWEET_INTENT";
    private TwitterLoginButton loginButton;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApi();
        switch (getIntent().getStringExtra(TWITTER_ACTION)) {
            case TWITTER_CONNECT:
                setContentView(R.layout.activity_twitter_login);
                setUpTwitterButton();
                break;
            case TWEET:
                TwitterSession twitterSession = gson.fromJson(getIntent().getStringExtra(TWITTER_SESSION),TwitterSession.class);
                Tweet tweet = gson.fromJson(getIntent().getStringExtra(TWEET),Tweet.class);
                tweet(twitterSession, tweet);
                break;
        }
    }

    private void initializeApi() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public static Intent tweetIntentBuilder(Intent intent, String tweet, String twitterAccount) {
        intent.putExtra(TwitterActivity.TWITTER_ACTION, TwitterActivity.TWEET);
        intent.putExtra(TwitterActivity.TWITTER_SESSION, twitterAccount);
        intent.putExtra(TwitterActivity.TWEET, tweet);
        return intent;
    }

    private void tweet(TwitterSession session, Tweet tweet) {
        final Intent intent = new ComposerActivity.Builder(this)
                .session(session)
                .text(tweet.getBody())
                .hashtags(tweet.getHashtags())
                .createIntent();


        startActivityForResult(intent, TWEET_REQUEST);

    }

    private void setUpTwitterButton() {
        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Result provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                Intent intent = new Intent();
                intent.putExtra(TWITTER_SESSION, gson.toJson(session));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                exception.getCause();
                Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        //To avoid the user to click on it
        loginButton.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TWEET_REQUEST){
            finish();
        } else {
            loginButton.onActivityResult(requestCode, resultCode, data);
            finish();
        }

    }

}

