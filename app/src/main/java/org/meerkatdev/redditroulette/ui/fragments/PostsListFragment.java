package org.meerkatdev.redditroulette.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsListActivity;
import org.meerkatdev.redditroulette.adapters.PostRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.databinding.FragmentPostsListBinding;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.ui.SubredditsSharedViewModel;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private SubredditsSharedViewModel subredditsSharedViewModel;
    private FragmentPostsListBinding binding;
    private String accessToken;
    private boolean mTwoPane;
    public PostsListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = FragmentPostsListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        setupRecyclerView(rootView);
        mTwoPane = getResources().getBoolean(R.bool.is_tablet);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Setting up subredditsSharedViewModel");
        Intent intent = getActivity().getIntent();
        if(intent != null) {
            if (mTwoPane) {
                subredditsSharedViewModel = ViewModelProviders
                        .of(getActivity())
                        .get(SubredditsSharedViewModel.class);
                // When the subreddits get downloaded and saved in the adapter
                subredditsSharedViewModel.getStoredSubreddits().observe(getViewLifecycleOwner(), elements -> {
                    Log.d(TAG, "om nom nom");
                    Bundle b = new Intent(intent).getExtras();
                    b.putString(Tags.SUBREDDIT_NAME, elements.get(0).name);
                    Tags.logBundle(b);
                    onViewCreation(b);
                });
                // When the Subreddit selection in the other fragment changes
                subredditsSharedViewModel.getSelectedSubreddit().observe(getViewLifecycleOwner(), element -> {
                    Bundle b = new Intent(intent).getExtras();
                    b.putString(Tags.SUBREDDIT_NAME, element.name);
                    Tags.logBundle(b);
                    onViewCreation(b);
                });
            } else {
                onViewCreation(intent.getExtras());
            }
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerView(View rootView) {
        final RecyclerView recyclerView = (RecyclerView)rootView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mTwoPane ? 2 : 1));
        viewAdapter = new PostRecyclerViewAdapter(getActivity(), mTwoPane);
        recyclerView.setAdapter(viewAdapter);
    }


    private void onViewCreation(Bundle args) {
        Log.d(TAG, "onViewCreation");
        if (args != null) {
            accessToken = args.getString(Tags.ACCESS_TOKEN);
            String mSubredditName = args.getString(Tags.SUBREDDIT_NAME);
            if(!mTwoPane) {
                CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.detail_toolbar);
                if (appBarLayout != null) {
                    appBarLayout.setTitle("/r/" + mSubredditName);
                }
            }
            boolean includeSfw =
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .getBoolean("sfw", false);

            Request request = RedditApi.getSubredditArticles(getContext(), mSubredditName, accessToken);
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

                    ArrayList<Post> elements = JSONUtils.parseJsonPosts(jsonResp);
                    if(elements != null)
                        getActivity().runOnUiThread(() ->
                            viewAdapter.setData(includeSfw ? elements
                                    : (ArrayList<Post>) elements.stream().filter(a -> !a.notSfw).collect(Collectors.toList()))
                        );
                }
            });
        }
    }
}
