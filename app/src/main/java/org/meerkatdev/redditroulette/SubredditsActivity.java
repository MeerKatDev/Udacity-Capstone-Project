package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.adapters.SubredditRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostsListActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SubredditsActivity extends AppCompatActivity {

    private static final String TAG = SubredditsActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddits_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO welcome username!
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        );

        Bundle extras = getIntent().getExtras();
        // Something is no yes, go back to MainActivity
        if (!extras.containsKey(Tags.ACCESS_TYPE)) finish();

        String accessToken = extras.getString(Tags.ACCESS_TOKEN);
        Log.d(TAG, "accessToken: " + accessToken);
        // guest!
        Request request = RedditApi.getApiSimpleRequest("subreddits", "popular", accessToken);

        RedditApi.client.newCall(request).enqueue(new RedditCallback(this, accessToken).invoke());
         //   mTwoPane = getResources().getBoolean(R.bool.is_tablet);
    }

    private class RedditCallback {
        private SubredditsActivity activityCtx;
        private String mAccessToken;

        public RedditCallback(SubredditsActivity activityCtx, String accessToken) {
            this.activityCtx = activityCtx;
            this.mAccessToken = accessToken;
        }

        public Callback invoke() {
            return new Callback() {

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
                    Log.d(TAG, "OnResponse, HTTP code: " + response.code());
                    Log.d(TAG, jsonResp);

                    if(response.code() == 401)
                        goBackWithShame(activityCtx, "Unauthorized");
                    else if(!response.header("content-type").equals("application/json; charset=UTF-8"))
                        goBackWithShame(activityCtx, "This is not json");
                    Subreddit[] arraySubs = JSONUtils.parseJsonSubreddits(jsonResp);
                    if(arraySubs != null)
                        runOnUiThread(() ->
                            setupRecyclerView(new ArrayList<>(Arrays.asList(arraySubs)))
                        );
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d(TAG, "OnFailure: " + e);
                }
            };
        }

        private void goBackWithShame(Activity activityCtx, String reason) {
            Intent backIntent = new Intent(activityCtx, MainActivity.class);
            backIntent.putExtra("error", reason);
            startActivity(backIntent);
        }

        private void setupRecyclerView(ArrayList<Subreddit> subreddits) {
            RecyclerView recyclerView = findViewById(R.id.rv_subreddits);
            recyclerView.setAdapter(new SubredditRecyclerViewAdapter(activityCtx, subreddits, mTwoPane, mAccessToken));
        }
    }
}
