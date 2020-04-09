package org.meerkatdev.redditroulette.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsListActivity;
import org.meerkatdev.redditroulette.adapters.SubredditRecyclerViewAdapter;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.databinding.FragmentSubredditsListBinding;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.ui.SubredditsSharedViewModel;
import org.meerkatdev.redditroulette.utils.JSONUtils;
import org.meerkatdev.redditroulette.utils.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SubredditsListFragment extends Fragment {

    private static final String TAG = SubredditsListFragment.class.getSimpleName();
    private SubredditsListActivity parentActivity;
    private boolean mTwoPane;
    private FragmentSubredditsListBinding binding;

    public SubredditsListFragment() { }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubredditsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private SubredditsSharedViewModel subredditsSharedViewModel;
    private SubredditRecyclerViewAdapter viewAdapter;
    private String accessToken;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        RecyclerView rootView = (RecyclerView) view;
        parentActivity = (SubredditsListActivity) getActivity();
        mTwoPane = getResources().getBoolean(R.bool.is_tablet);
        Bundle extras = parentActivity.getIntent().getExtras();
        // Something is no yes, go back to MainActivity
        if (extras == null || (!extras.containsKey(Tags.ACCESS_TYPE))) {
            parentActivity.finish();
        } else {
            accessToken = extras.getString(Tags.ACCESS_TOKEN, "");
            viewAdapter = new SubredditRecyclerViewAdapter(parentActivity, mTwoPane, accessToken);
            rootView.setAdapter(viewAdapter);
            Log.d(TAG, "accessToken: " + accessToken);
            // guest!
            populateView(accessToken, viewAdapter);
        }

        super.onViewCreated(rootView, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void populateView(String accessToken, SubredditRecyclerViewAdapter viewAdapter) {

        Request request = RedditApi.getApiSimpleRequest("subreddits", "popular", accessToken);

        if(mTwoPane) {
            subredditsSharedViewModel = ViewModelProviders.of(getActivity()).get(SubredditsSharedViewModel.class);
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
                                subredditsSharedViewModel.saveSubreddits(elements);
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
}
