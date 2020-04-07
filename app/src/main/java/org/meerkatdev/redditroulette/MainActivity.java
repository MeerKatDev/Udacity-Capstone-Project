package org.meerkatdev.redditroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.meerkatdev.redditroulette.net.OAuthHandler;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.utils.Tags;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String randomString = "random_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.LIFECYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        if(intent != null && intent.getExtras() != null
            && intent.getExtras().containsKey("error")) {
            // Splash screen!
            Log.e(TAG, "Message: " + intent.getStringExtra("error"));
        } else {
            tryToGoAsRegisteredUser();
        }

        // first time, nonregistered, just go
        findViewById(R.id.asguest).setOnClickListener(v -> {
            Intent internalIntent = new Intent(this, SubredditsActivity.class);
            intent.putExtra(Tags.ACCESS_TYPE, "guest");
            startActivity(internalIntent);
        });
        // first time, registered, but they have to login
        findViewById(R.id.signin).setOnClickListener(v -> {
            Uri authUrl = RedditApi.buildAuthorizationURL(randomString);
            Intent internalIntent = new Intent(Intent.ACTION_VIEW, authUrl);
            startActivity(internalIntent);
        });
    }

    private void tryToGoAsRegisteredUser() {
        SharedPreferences oauthPreference = getSharedPreferences("oauth", Context.MODE_PRIVATE);
        if(oauthPreference.contains(Tags.ACCESS_TOKEN)) {
            String accessToken = oauthPreference.getString(Tags.ACCESS_TOKEN, "");
            Intent viewIntent = new Intent(this, SubredditsActivity.class);
            viewIntent.putExtra(Tags.ACCESS_TYPE, "registered_user");
            viewIntent.putExtra(Tags.ACCESS_TOKEN, accessToken);
            startActivity(viewIntent);
        }
    }

    @Override
    protected void onResume() {
        Log.d(Tags.LIFECYCLE, "onResume");
        super.onResume();
        Intent intent = getIntent();
        if(intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            OAuthHandler handler = new OAuthHandler(this);
            handler.handleReturnUri(intent.getData(), randomString);
            tryToGoAsRegisteredUser();
        }
    }

}
