package org.meerkatdev.redditroulette.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 https://progur.com/2016/10/how-to-use-reddit-oauth2-in-android-apps.html
 */
public class OAuthHandler {

    final Context ctx;

    public OAuthHandler(Context ctx) {
        this.ctx = ctx;
    }

    private final String TAG = this.getClass().getSimpleName();

    public void handleReturnUri(Uri uri, String randomString) {

        if(Objects.requireNonNull(uri).getQueryParameter("error") != null) {
            String error = uri.getQueryParameter("error");
            Log.e(TAG, "An error has occurred : " + error);
        } else {
            String state = uri.getQueryParameter("state");
            if(state.equals(randomString)) {
                String code = uri.getQueryParameter("code");
                new Thread(() -> getAccessToken(code)).start();
            }
        }
    }

    public void getAccessToken(String code){

        Log.d(TAG, "Getting access token!");

        Request request = RedditApi.getAuthorizationTokenRequest(ctx, code);
        RedditApi.client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @SuppressLint("ApplySharedPref")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = Objects.requireNonNull(response.body(), "body is null").string();

                JSONObject data = null;

                try {
                    data = new JSONObject(json);
                    String accessToken = data.optString(Tags.ACCESS_TOKEN);
                    String scope = data.optString("scope");
                    String refreshToken = data.optString("refresh_token");
                    String expiresIn = data.optString("expires_in");
                    String tokenType = data.optString("token_type");

                    Log.d(TAG, "Access Token = " + accessToken);
                    Log.d(TAG, "Refresh Token = " + refreshToken);
                    Log.d(TAG, "expiresIn = " + expiresIn);
                    Log.d(TAG, "tokenType = " + tokenType);
                    Log.d(TAG, "scope = " + scope);


                    SharedPreferences sharedPref = ctx.getSharedPreferences("oauth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Tags.ACCESS_TOKEN, accessToken);
                    editor.putString("refresh_token", refreshToken);
                    editor.putString("scope", scope);
                    editor.putString("token_type", tokenType);
                    editor.putString("expires_in", expiresIn);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
