package org.meerkatdev.redditroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.databinding.ActivityMainBinding;
import org.meerkatdev.redditroulette.net.OAuthHandler;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

// TODO Widget selecting one Subreddit only // need to test probably

// TODO TESTING
// TODO adapt for tablets (almost)
// TODO build cache for offline navigation
// TODO Dark/Light themes

// For compatibility with

// Mockito
// https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
// refactoring
// implement Ad as fragment
// move onClick Handling to fragments
// add saving list state
// write to the reviewer that the tasks were handled by the OkhTTP

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String randomString = "random_string";
    private Context mContext;
    private SharedPreferences oauthPreference;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.LIFECYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        oauthPreference = getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
        mContext = this;
        // not first access
        if(preferenceHasNonEmptyToken()) {
            Log.d(Tags.LIFECYCLE, "it contains the access token, and");
            if(isTokenExpired()) {
                Log.d(Tags.LIFECYCLE, "it is expired, refreshing access token!");
                String refreshToken = oauthPreference.getString(Tags.REFRESH_TOKEN, "");
                handleAccessTokenCall(RedditApi.getRefreshTokenRequest(mContext, refreshToken));
            } else {
                Log.d(TAG, "Remained: " + getSecondsToExpiration() + "seconds, so it is not expired!");
                goAsRegisteredUser(oauthPreference.getString(Tags.ACCESS_TOKEN, ""));
            }
        } else {
            Log.d(Tags.LIFECYCLE, "No access token in memory!");
        }
        Intent intent = getIntent();

        binding.asguest.setOnClickListener(v -> {
            if(preferenceHasNonEmptyToken() && !isTokenExpired()) {
                goAsRegisteredUser(oauthPreference.getString(Tags.ACCESS_TOKEN, ""));
            } else {
                // first time, nonregistered, get new token
                Intent internalIntent = new Intent(this, SubredditsListActivity.class);
                intent.putExtra(Tags.ACCESS_TYPE, "guest");
                startActivity(internalIntent);
            }
        });
        // first time, registered, but they have to login
        binding.signin.setOnClickListener(v -> {
            Uri authUrl = RedditApi.buildAuthorizationURL(randomString);
            Intent internalIntent = new Intent(Intent.ACTION_VIEW, authUrl);
            startActivity(internalIntent);
        });
    }

    private boolean preferenceHasNonEmptyToken() {
        return oauthPreference.contains(Tags.ACCESS_TOKEN)
                && !oauthPreference.getString(Tags.ACCESS_TOKEN, "").isEmpty();
    }

    private long getSecondsToExpiration() {
        return (oauthPreference.getLong("expires_at",0L) - System.currentTimeMillis()/1000);
    }

    private boolean isTokenExpired() {
        return oauthPreference.getLong("expires_at", 0L) <= (System.currentTimeMillis()/1000);
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
