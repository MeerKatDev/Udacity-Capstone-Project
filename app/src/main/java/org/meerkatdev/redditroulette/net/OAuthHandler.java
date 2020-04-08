package org.meerkatdev.redditroulette.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/*
 https://progur.com/2016/10/how-to-use-reddit-oauth2-in-android-apps.html
 */
public class OAuthHandler {

    final Context ctx;

    public OAuthHandler(Context ctx) {
        this.ctx = ctx;
    }

    private static final String TAG = OAuthHandler.class.getSimpleName();

    public static JSONObject readTokenResponse(String textResponse) {
        try {
            return new JSONObject(textResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }

    public static void saveOauthDataFromJson(Context ctx, JSONObject data, long seconds) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
        String accessToken = data.optString(Tags.ACCESS_TOKEN);
        String scope = data.optString("scope");
        String refreshToken = data.optString("refresh_token");
        int expiresIn = data.optInt("expires_in");
        String tokenType = data.optString("token_type");

        Log.d(TAG, "Access Token = " + accessToken);
        Log.d(TAG, "Refresh Token = " + refreshToken);
        Log.d(TAG, "scope = " + scope);

        if(!tokenType.equals("bearer")) {
            Log.e(TAG, "The token type is not bearer, but '" + tokenType + "'");
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Tags.ACCESS_TOKEN, accessToken);
        editor.putString("scope", scope);
        editor.putLong("expires_at", seconds + ((long)expiresIn));
        if(!refreshToken.isEmpty())
            editor.putString("refresh_token", refreshToken);
        editor.apply();

    }

}
