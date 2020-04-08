package org.meerkatdev.redditroulette.net.callbacks;

import android.app.Activity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.adapters.RVAdapter;
import org.meerkatdev.redditroulette.utils.JSONUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// TODO finish this
public class RedditCallback<T> implements Callback {

    private static final String TAG = Callback.class.getSimpleName();
    private final Activity outerActivity;
    private final RVAdapter viewAdapter;
    private final Class<T> tClass;

    public RedditCallback(Activity outerActivity, RVAdapter viewAdapter, Class<T> tClass) {
        this.outerActivity = outerActivity;
        this.viewAdapter = viewAdapter;
        this.tClass = tClass;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.d(TAG, "OnFailure: " + e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
        Log.d(TAG, "OnResponse, HTTP code: " + response.code());
        Log.d(TAG, jsonResp);

        //ArrayList<T> arraySubs = JSONUtils.parseJsonPosts(jsonResp);
//        if(arraySubs != null)
//            outerActivity.runOnUiThread(() -> {
//                viewAdapter.setData(arraySubs);
//            });
    }
}
