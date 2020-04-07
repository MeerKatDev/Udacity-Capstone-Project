package org.meerkatdev.redditroulette.net;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.meerkatdev.redditroulette.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RedditApi implements RedditConstants {

    private static final String TAG = RedditApi.class.getSimpleName();

    private static final String CLIENT_ID = "a0yCeVL5YxKt_Q";
    private static final String REDIRECT_URI = "http://localhost:4321";
    private static final String SCOPE = "flair,mysubreddits,read";
    private static final String API_REQUESTS_BASE_PATH = "https://oauth.reddit.com/";
    private static final String API_REQUESTS_BASE_PATH_V1 = "https://oauth.reddit.com/api/v1";

    public static OkHttpClient client = new OkHttpClient();

    public static Uri buildAuthorizationURL(String randomString) {
        return Uri.parse(REDDIT_API_BASEPATH).buildUpon()
                .appendPath("authorize")
                .appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID)
                .appendQueryParameter(RESPONSE_TYPE_KEY, "code")
                .appendQueryParameter(STATE_KEY, randomString)
                .appendQueryParameter(REDIRECT_URI_KEY, REDIRECT_URI)
                .appendQueryParameter(DURATION_KEY, "permanent")
                .appendQueryParameter(SCOPE_KEY, SCOPE)
                .build();
    }

    static Request getAuthorizationTokenRequest(Context ctx, String code) {

        String authString = CLIENT_ID + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

        MediaType mediumType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody requestBody = RequestBody.Companion.create(
                "grant_type=authorization_code&code=" + code +
                        "&redirect_uri=" + REDIRECT_URI, mediumType);

        String accessTokenUrl = Uri.parse(REDDIT_API_BASEPATH).buildUpon()
                .appendPath("access_token")
                .build().toString();

        return new Request.Builder()
                .addHeader("User-Agent", ctx.getResources().getString(R.string.app_name))
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .url(accessTokenUrl)
                .post(requestBody)
                .build();
    }

    public static Request getSubscribedSubreddits(String authToken) {
        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        String builtRequestUrl = accessTokenUrl.appendEncodedPath("subreddits/mine/subscriber").build().toString();
        return generatedAuthorizedRequest(builtRequestUrl, authToken);
    }

    public static Request getApiSimpleRequest(String objects, String param, String authToken) {

        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        accessTokenUrl.appendQueryParameter("limit", "10");
        accessTokenUrl.appendPath(objects);

        if (!param.isEmpty())
            accessTokenUrl.appendPath(param);

        String builtRequestUrl = accessTokenUrl.build().toString();
        return generatedAuthorizedRequest(builtRequestUrl, authToken);
    }

    private static Request generatedAuthorizedRequest(String builtRequestUrl, String authToken) {
        Log.d(TAG, "Request made to: " + builtRequestUrl);
        Log.d(TAG, "authToken: " + authToken);
        return new Request.Builder()
                .addHeader("Authorization", "bearer " + authToken)
                .url(builtRequestUrl)
                .build();
    }

}