package org.meerkatdev.redditroulette.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsListActivity;
import org.meerkatdev.redditroulette.adapters.SubredditRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.data.db.AppDatabase;
import org.meerkatdev.redditroulette.databinding.FragmentSubredditsListBinding;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.ui.SubredditsSharedViewModel;
import org.meerkatdev.redditroulette.utils.AppExecutors;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SubredditsListFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = SubredditsListFragment.class.getSimpleName();
    private SubredditsListActivity parentActivity;
    private boolean mTwoPane;
    private FragmentSubredditsListBinding binding;

    public SubredditsListFragment() { }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubredditsListBinding.inflate(inflater, container, false);
        parentActivity = (SubredditsListActivity) getActivity();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(parentActivity);
        return binding.getRoot();
    }

    private SubredditsSharedViewModel subredditsSharedViewModel;
    private SubredditRecyclerViewAdapter viewAdapter;
    private String accessToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        RecyclerView rootView = (RecyclerView) view;
        mTwoPane = getResources().getBoolean(R.bool.is_tablet);
        Bundle extras = parentActivity.getIntent().getExtras();
        mFirebaseAnalytics.logEvent("opened_subreddits_fragment", extras);
        if (extras == null || (!extras.containsKey(Tags.ACCESS_TYPE))) {
            // Something is no yes, go back to MainActivity
            parentActivity.finish();
        } else {
            accessToken = extras.getString(Tags.ACCESS_TOKEN, "");
            viewAdapter = new SubredditRecyclerViewAdapter(parentActivity, mTwoPane, accessToken);
            rootView.setAdapter(viewAdapter);
            Log.d(TAG, "accessToken: " + accessToken);
            // guest!
            Request request = RedditApi.getApiSimpleRequest("subreddits", "popular", accessToken);
            populateView(viewAdapter, request);
        }

        super.onViewCreated(rootView, savedInstanceState);
    }

    private void populateView(SubredditRecyclerViewAdapter viewAdapter, Request request) {
        boolean includeSfw =
                PreferenceManager.getDefaultSharedPreferences(parentActivity)
                        .getBoolean("sfw", false);

        if(mTwoPane) {
            subredditsSharedViewModel = ViewModelProviders.of(parentActivity).get(SubredditsSharedViewModel.class);
            viewAdapter.setViewModel(subredditsSharedViewModel);
        }
        RedditApi.client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
                Log.d(TAG, "OnResponse, HTTP code: " + response.code());
                Log.d(TAG, jsonResp);

                if(response.code() == 401)
                    parentActivity.goBackWithShame(parentActivity, "Unauthorized");
                else if(!response.header("content-type").equals("application/json; charset=UTF-8"))
                    parentActivity.goBackWithShame(parentActivity, "This is not json");
                else {
                    ArrayList<Subreddit> elements = JSONUtils.parseJsonSubreddits(jsonResp);
                    if (!elements.isEmpty())
                        parentActivity.runOnUiThread(() -> {
                            if(mTwoPane) {
                                Log.d(TAG, "Updating!");
                                subredditsSharedViewModel.saveSubreddits(includeSfw ? elements
                                        : (ArrayList<Subreddit>) elements.stream().filter(a -> !a.notSfw).collect(Collectors.toList()));
                            }
                            viewAdapter.setData(elements);
                        });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "OnFailure: " + e);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Request request;
        Log.d(TAG, "selected item");
        switch(item.getItemId()) {
            case R.id.sr_sortby_new:
                request = RedditApi.getApiSimpleRequest("subreddits", "new", accessToken);
                populateView(viewAdapter, request);
                break;
            case R.id.sr_sortby_subscribed:
                request = RedditApi.getSubscribedSubreddits(accessToken);
                populateView(viewAdapter, request);
                break;
            case R.id.sr_sortby_saved:
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Log.d(TAG, "Getting saved subreddits ");
                    List<Subreddit> elements = AppDatabase.getInstance(parentActivity).subredditDAO().loadAll().getValue();
                    viewAdapter.setData(elements);
                });
                break;
            default: // popular is default
                request = RedditApi.getApiSimpleRequest("subreddits", "popular", accessToken);
                populateView(viewAdapter, request);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
