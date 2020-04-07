package org.meerkatdev.redditroulette.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.data.Subreddit;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    public static Subreddit[] parseJsonSubreddits(String jsonString) {
        try {
            JSONObject fullResponse = new JSONObject(jsonString);
            JSONObject data = fullResponse.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("children");
            Subreddit[] subreddits = new Subreddit[jsonArray.length()];
            JSONObject dataInternal = null;
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
}
