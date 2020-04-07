package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.dummy.DummyContent;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SubredditsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
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
        Activity activityCtx = this;
        // guest!
        Request request = RedditApi.getApiSimpleRequest("subreddits", "popular", accessToken);

        RedditApi.client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
                Log.d(TAG, "onResponse, code: " + response.code());
                Log.d(TAG, jsonResp);

                if(response.code() == 401)
                    goBackWithShame(activityCtx, "Unauthorized");
                else if(!response.header("content-type").equals("application/json; charset=UTF-8"))
                    goBackWithShame(activityCtx, "This is not json");

                Subreddit[] subreddits = JSONUtils.parseJsonSubreddits(jsonResp);
                if(subreddits!=null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            setupRecyclerView(new ArrayList<>(Arrays.asList(subreddits)));
                        }
                    });

                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, String.valueOf(e));
            }
        });

        // if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
         //   mTwoPane = true;
        // }

    }

    private void goBackWithShame(Activity activityCtx, String reason) {
        Intent backIntent = new Intent(activityCtx, MainActivity.class);
        backIntent.putExtra("error", reason);
        startActivity(backIntent);
    }

    private void setupRecyclerView(ArrayList<Subreddit> subreddits) {
        RecyclerView recyclerView = findViewById(R.id.item_list);
        recyclerView.setAdapter(new SubredditRecyclerViewAdapter(this, subreddits, mTwoPane));
    }

    public static class SubredditRecyclerViewAdapter
            extends RecyclerView.Adapter<SubredditRecyclerViewAdapter.ViewHolder> {

        private final SubredditsActivity mParentActivity;
        private final List<Subreddit> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subreddit item = (Subreddit) view.getTag();
                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(PostFragment.ARG_ITEM_ID, item.redditId);
//                    PostFragment fragment = new PostFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, PostActivity.class);
                    // Sending info about subreddit
                    intent.putExtra(PostFragment.ARG_ITEM_ID, item.redditId);

                    context.startActivity(intent);
                }
            }
        };

        SubredditRecyclerViewAdapter(SubredditsActivity parent,
                                     List<Subreddit> items,
                                     boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subreddit_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).redditId);
            holder.mContentView.setText(mValues.get(position).name);
            String imagePath = mValues.get(position).iconImg;
            if(imagePath != null && !imagePath.isEmpty())
                Picasso.get().load(mValues.get(position).iconImg).into(holder.mIconView);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mIconView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                mIconView = view.findViewById(R.id.iv_subreddit_icon);
            }
        }
    }
}
