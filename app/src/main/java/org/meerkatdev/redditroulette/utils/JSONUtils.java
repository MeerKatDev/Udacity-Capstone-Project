package org.meerkatdev.redditroulette.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.redditroulette.data.*;

import java.util.ArrayList;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();
    private static Gson gson = new Gson();

    public static JSONArray getComposedObjectInternalArray(String jsonString) {
        try {
            JSONArray fullArrayResponse = new JSONArray(jsonString); // first (0) is the parent
            JSONObject data = fullArrayResponse.getJSONObject(1).getJSONObject("data");
            return data.getJSONArray("children");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception: " + e);
            return new JSONArray();
        }
    }

    public static JSONArray getStdInternalArray(String jsonString) {
        try {
            JSONObject fullResponse = new JSONObject(jsonString);
            JSONObject data = fullResponse.getJSONObject("data");
            return data.getJSONArray("children");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception: " + e);
            return new JSONArray();
        }
    }

    public static ArrayList<Subreddit> parseJsonSubreddits(String jsonString) {
        return parseGenListing(getStdInternalArray(jsonString), Subreddit.class);
    }

    public static ArrayList<Post> parseJsonPosts(String jsonString) {
        return parseGenListing(getStdInternalArray(jsonString), Post.class);
    }

    public static ArrayList<Comment> parseJsonComments(String jsonString) {
        return parseGenListing(getComposedObjectInternalArray(jsonString), Comment.class);
    }

    private static <T> ArrayList<T> parseGenListing(JSONArray jsonArray, Class<T> tClass) {
        try {
            ArrayList<T> arrayList = new ArrayList<>();
            JSONObject dataInternal;
            for (int i = 0; i < jsonArray.length(); i++) {
                dataInternal = jsonArray.getJSONObject(i).getJSONObject("data");
                // the other field is "kind" (e.g. t_1, t_3, t_5)
                arrayList.add(gson.fromJson(String.valueOf(dataInternal), tClass));
                Log.d(TAG, arrayList.get(i).toString());
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
