package org.meerkatdev.redditroulette.net;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.utils.Tags;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RedditApi implements RedditConstants {

    private static final String TAG = RedditApi.class.getSimpleName();

    public static final String REDDIT_URL = "https://www.reddit.com";

    private static final String CLIENT_ID = "a0yCeVL5YxKt_Q";
    private static final String REDIRECT_URI = "http://localhost:4321";
    private static final String RESPONSE_TYPE = "code";
    private static final String PERMANENT = "permanent";
    private static final String SCOPE = "flair,mysubreddits,read";
    private static final String API_REQUESTS_BASE_PATH = "https://oauth.reddit.com/";

    public static OkHttpClient client = new OkHttpClient();

    public static Uri buildAuthorizationURL(String randomString) {
        return Uri.parse(REDDIT_API_BASEPATH).buildUpon()
                .appendPath("authorize")
                .appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID)
                .appendQueryParameter(RESPONSE_TYPE_KEY, RESPONSE_TYPE)
                .appendQueryParameter(STATE_KEY, randomString)
                .appendQueryParameter(REDIRECT_URI_KEY, REDIRECT_URI)
                .appendQueryParameter(DURATION_KEY, PERMANENT)
                .appendQueryParameter(SCOPE_KEY, SCOPE)
                .build();
    }

    public static Request getRefreshTokenRequest(Context ctx, String refreshToken) {

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();

        Log.d(TAG, requestBody.toString());
        Log.d(TAG, "refresh_token: " + refreshToken);

        String accessTokenUrl = getAuthorizationUrl();

        Log.d(TAG, "Link: " + accessTokenUrl);
        return new Request.Builder()
                .addHeader("User-Agent", ctx.getResources().getString(R.string.app_name))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", getAuthorizationHeader())
                .url(accessTokenUrl)
                .post(requestBody)
                .build();
    }

    public static Request getAuthorizationTokenRequest(Context ctx, String code) {


        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", REDIRECT_URI)
                .build();

        String accessTokenUrl = getAuthorizationUrl();

        return new Request.Builder()
                .addHeader("User-Agent", ctx.getResources().getString(R.string.app_name))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", getAuthorizationHeader())
                .url(accessTokenUrl)
                .post(requestBody)
                .build();
    }

    private static String getAuthorizationHeader() {
        String authString = CLIENT_ID + ":";
        return "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
    }

    private static String getAuthorizationUrl() {
        return Uri.parse(REDDIT_API_BASEPATH).buildUpon()
                .appendPath(Tags.ACCESS_TOKEN)
                .build().toString();
    }

    public static Request getRandomSubredditArticle(String subreddit, String authToken) {
        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        String builtRequestUrl = accessTokenUrl.appendEncodedPath("r/"+subreddit+"/random.json")
                .build().toString();
        return generatedAuthorizedRequest(builtRequestUrl, authToken);
    }

    public static Request getArticleComments(String subreddit, String articleId, String authToken) {
        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        String builtRequestUrl = accessTokenUrl.appendEncodedPath("r/"+subreddit+"/comments/"+articleId+"/")
                .build().toString();
        return generatedAuthorizedRequest(builtRequestUrl, authToken);
    }


    public static Request getSubredditArticles(String subreddit, String authToken) {
        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        String builtRequestUrl = accessTokenUrl.appendEncodedPath("r/"+subreddit+"/new.json")
                .appendQueryParameter("limit", "10")
                .build().toString();
        return generatedAuthorizedRequest(builtRequestUrl, authToken);
    }

    public static Request getSubscribedSubreddits(String authToken) {
        Uri.Builder accessTokenUrl = Uri.parse(API_REQUESTS_BASE_PATH).buildUpon();
        String builtRequestUrl = accessTokenUrl.appendEncodedPath("subreddits/mine/subscriber")
                .build().toString();
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

    private static Request generatedAuthorizedRequest(String builtRequestUrl, String accessToken) {
        Log.d(TAG, "Request made to: " + builtRequestUrl);
        Log.d(TAG, "authToken: " + accessToken);
        return new Request.Builder()
                .addHeader("Authorization", "bearer " + accessToken)
                .url(builtRequestUrl)
                .build();
    }

}