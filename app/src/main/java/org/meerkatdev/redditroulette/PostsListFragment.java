package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.adapters.PostRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Post;
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
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link SubredditsActivity}
 * in two-pane mode (on tablets) or a {@link PostsListActivity}
 * on handsets.
 */
public class PostsListFragment extends Fragment {

    private static final String TAG = PostsListFragment.class.getSimpleName();
    private PostRecyclerViewAdapter viewAdapter;
    public PostsListFragment() { }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//            onViewCreation(getArguments());
//    }

    private void onViewCreation(Bundle args) {
        if (args != null && args.containsKey(Tags.SUBREDDIT_NAME)) {
            String accessToken = args.getString(Tags.ACCESS_TOKEN);
            String mSubredditName = args.getString(Tags.SUBREDDIT_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("/r/" + mSubredditName);
            }

            Request request = RedditApi.getSubredditArticles(mSubredditName, accessToken);
            RedditApi.client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d(TAG, "OnFailure: " + e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
                    Log.d(TAG, "OnResponse, HTTP code: " + response.code());
                    Log.d(TAG, jsonResp);

                    ArrayList<Post> arraySubs = JSONUtils.parseJsonPosts(jsonResp);
                    if(arraySubs != null)
                        getActivity().runOnUiThread(() -> {
                            viewAdapter.setData(arraySubs);
                            //recyclerView.setAdapter(new PostRecyclerViewAdapter((PostsListActivity) getActivity());
                            //setupRecyclerView(new ArrayList<>(Arrays.asList(arraySubs)))
                        });
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        setupRecyclerView(rootView);
        //recyclerView.setAdapter(new PostRecyclerViewAdapter((PostsListActivity)getActivity(), new ArrayList<>()));
        onViewCreation(getActivity().getIntent().getExtras());
        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ( (TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
//        }

        return rootView;
    }

    private void setupRecyclerView(View rootView) {
        final RecyclerView recyclerView = rootView.findViewById(R.id.rv_posts);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        viewAdapter = new PostRecyclerViewAdapter((PostsListActivity)getActivity());
        recyclerView.setAdapter(viewAdapter);
    }
}
