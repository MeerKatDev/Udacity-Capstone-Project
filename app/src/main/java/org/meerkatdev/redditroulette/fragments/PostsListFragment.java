package org.meerkatdev.redditroulette.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsListActivity;
import org.meerkatdev.redditroulette.adapters.PostRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.ui.SharedViewModel;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link SubredditsListActivity}
 * in two-pane mode (on tablets) or a {@link PostsListActivity}
 * on handsets.
 */
public class PostsListFragment extends Fragment {

    private static final String TAG = PostsListFragment.class.getSimpleName();
    private PostRecyclerViewAdapter viewAdapter;
    private boolean mTwoPane;
    public PostsListFragment() { }

    private void onViewCreation(Bundle args) {
        Log.d(TAG, "onViewCreation");
        if (args != null) {
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
                        });
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        setupRecyclerView(rootView);
        mTwoPane = getResources().getBoolean(R.bool.is_tablet);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "Setting up sharedViewModel");
        if(mTwoPane) {
            ViewModelProviders.of(getActivity())
                    .get(SharedViewModel.class)
                    .getStoredSubreddits()
                    .observe(getViewLifecycleOwner(), elements -> {
                        Log.d(TAG, "om nom nom");
                        Log.d(TAG, String.valueOf(elements.size()));
                        Bundle b = new Intent(getActivity().getIntent()).getExtras();
                        b.putString(Tags.SUBREDDIT_NAME, elements.get(0).name);
                        Tags.logBundle(b);
                        onViewCreation(b);
                    });
        } else {
            onViewCreation(getActivity().getIntent().getExtras());
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerView(View rootView) {
        final RecyclerView recyclerView = (RecyclerView)rootView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mTwoPane ? 2 : 1));
        viewAdapter = new PostRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(viewAdapter);
    }
}