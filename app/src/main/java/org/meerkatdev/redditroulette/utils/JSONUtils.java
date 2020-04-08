package org.meerkatdev.redditroulette.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.data.*;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();


    private static JSONArray getComposedObjectInternalArray(String jsonString) throws JSONException {
        JSONArray fullArrayResponse = new JSONArray(jsonString); // first (0) is the parent
        JSONObject data = fullArrayResponse.getJSONObject(1).getJSONObject("data");
        return data.getJSONArray("children");
    }

    private static JSONArray getStdInternalArray(String jsonString) throws JSONException {
        JSONObject fullResponse = new JSONObject(jsonString);
        JSONObject data = fullResponse.getJSONObject("data");
        return data.getJSONArray("children");
    }

    public static Subreddit[] parseJsonSubreddits(String jsonString) {
        try {
            JSONArray jsonArray = getStdInternalArray(jsonString);
            Subreddit[] subreddits = new Subreddit[jsonArray.length()];
            JSONObject dataInternal;
            for (int i = 0; i < jsonArray.length(); i++) {
                dataInternal = jsonArray.getJSONObject(i).getJSONObject("data");
                // the other field is "kind"
                subreddits[i] = new Subreddit(
                        dataInternal.getString("id"),
                        dataInternal.getString("display_name"),
                        dataInternal.getString("description"),
                        dataInternal.getString("icon_img"),
                        dataInternal.getString("header_img"),
                        dataInternal.getBoolean("over18")
                );
                Log.d(TAG, subreddits[i].toString());
            }
            return subreddits;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Post[] parseJsonPosts(String jsonString) {
        try {
            //JSONArray fullResponse = new JSONArray(jsonString);
            //JSONObject data = fullResponse.getJSONObject(0).getJSONObject("data");
            JSONArray jsonArray = getStdInternalArray(jsonString);
            Post[] subreddits = new Post[jsonArray.length()];
            JSONObject dataInternal = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                dataInternal = jsonArray.getJSONObject(i).getJSONObject("data");
                // the other field is "kind"
                subreddits[i] = new Post(
                        dataInternal.getString("id"),
                        dataInternal.getString("title"),
                        dataInternal.getString("author"),
                        dataInternal.getString("selftext"),
                        dataInternal.getString("permalink"),
                        dataInternal.getString("url"),
                        dataInternal.getString("subreddit"),
                        dataInternal.getBoolean("over_18"),
                        dataInternal.optString("post_hint", "")
                );
                Log.d(TAG, subreddits[i].toString());
            }
            return subreddits;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Comment[] parseJsonComments(String jsonString) {
        try {
            JSONArray jsonArray = getComposedObjectInternalArray(jsonString);
            Comment[] comments = new Comment[jsonArray.length()];
            JSONObject dataInternal;
            for (int i = 0; i < jsonArray.length(); i++) {
                dataInternal = jsonArray.getJSONObject(i).getJSONObject("data");
                comments[i] = new Comment(
                    dataInternal.getString("author"),
                    dataInternal.getString("body")
                );
                Log.d(TAG, comments[i].toString());
            }
            return comments;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
