package org.meerkatdev.redditroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.net.OAuthHandler;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

// TODO adapt for tablets
// TODO move onClick Handling to fragments

// TODO Widget selecting one Subreddit only
// TODO build cache for offline navigation
// TODO Favorites

// TODO Settings
// TODO add some tests

// TODO Dark/Light themes
// TODO RTL layout


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String randomString = "random_string";
    private Context mContext;
    private SharedPreferences oauthPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.LIFECYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oauthPreference =  getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
        mContext = this;
        // not first access
        if(oauthPreference.contains(Tags.ACCESS_TOKEN) && !oauthPreference.getString(Tags.ACCESS_TOKEN, "").isEmpty()) {
            Log.d(Tags.LIFECYCLE, "it contains the access token, and");
            if(isTokenExpired()) {
                Log.d(TAG, "Remained: " + (oauthPreference.getLong("expires_at",0L) - System.currentTimeMillis()/1000));
                Log.d(Tags.LIFECYCLE, "it is expired");
                String refreshToken = oauthPreference.getString(Tags.REFRESH_TOKEN, "");
                Log.d(TAG, "refreshing access token!");
                handleAccessTokenCall(RedditApi.getRefreshTokenRequest(mContext, refreshToken));
            } else {
                Log.d(TAG, "Remained: " + (oauthPreference.getLong("expires_at",0L) - System.currentTimeMillis()/1000));
                Log.d(Tags.LIFECYCLE, "it is not expired!");
                goAsRegisteredUser(oauthPreference.getString(Tags.ACCESS_TOKEN, ""));
            }
        } else {
            Log.d(Tags.LIFECYCLE, "No access token in memory!");
        }
        Intent intent = getIntent();

        // first time, nonregistered, just go
        findViewById(R.id.asguest).setOnClickListener(v -> {
            Intent internalIntent = new Intent(this, SubredditsListActivity.class);
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

    private boolean isTokenExpired() {
        return oauthPreference.getLong("expires_at", 0L) < (System.currentTimeMillis()/1000);
    }

    private void goAsRegisteredUser(String accessToken) {
        Log.d(TAG, "Remained: " + (oauthPreference.getLong("expires_at",0L) - System.currentTimeMillis()/1000));
        Intent viewIntent = new Intent(mContext, SubredditsListActivity.class);
        viewIntent.putExtra(Tags.ACCESS_TYPE, "registered_user");
        viewIntent.putExtra(Tags.ACCESS_TOKEN, accessToken);
        mContext.startActivity(viewIntent);
    }

    @Override
    protected void onResume() {
        Log.d(Tags.LIFECYCLE, "onResume");
        super.onResume();
        Intent intent = getIntent();
        if(intent != null) {
            Tags.logBundle(intent.getExtras());
            if(intent.getExtras() != null && intent.getExtras().containsKey("error")) {
                // Splash screen!
                Log.e(TAG, "Message: " + intent.getStringExtra("error"));
            }
            if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)
            && intent.getExtras().containsKey("com.android.browser.application_id")) {
                Log.e(TAG, "Going as registered user");
                handleReturnUri(intent.getData(), randomString);
            }
        }

    }

    public void handleReturnUri(Uri uri, String randomString) {
        if(Objects.requireNonNull(uri).getQueryParameter("error") != null) {
            String error = uri.getQueryParameter("error");
            Log.e(TAG, "An error has occurred : " + error);
        } else {
            String state = uri.getQueryParameter("state");
            if(state.equals(randomString)) {
                String code = uri.getQueryParameter("code");
                Log.d(TAG, "Getting access token!");
                handleAccessTokenCall(RedditApi.getAuthorizationTokenRequest(mContext, code));
            }
        }
    }

    public void handleAccessTokenCall(Request request){
        RedditApi.client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.body() != null) {
                    JSONObject jsonObject = OAuthHandler.readTokenResponse(response.body().string());
                    String accessToken = jsonObject.optString(Tags.ACCESS_TOKEN);
                    OAuthHandler.saveOauthDataFromJson(mContext, jsonObject, System.currentTimeMillis()/1000);
                    goAsRegisteredUser(accessToken);
                }
            }
        });
    }

}
