package org.meerkatdev.redditroulette;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.adapters.CommentRecyclerViewAdapter;
import org.meerkatdev.redditroulette.adapters.PostRecyclerViewAdapter;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.data.Comment;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.databinding.ActivityPostViewBinding;
import org.meerkatdev.redditroulette.databinding.ItemPostsListBinding;
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

public class PostViewActivity extends AppCompatActivity {

    private static final String TAG = PostViewActivity.class.getSimpleName();
    private CommentRecyclerViewAdapter viewCommentsAdapter;
    private ActivityPostViewBinding binding;
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostViewBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        Intent intent = getIntent();
        if(intent.getExtras().containsKey(Tags.POST)) {
            mPost = intent.getParcelableExtra(Tags.POST);
        }
        setupRecyclerView(rootView);
        downloadComments();
    }

    private void setupRecyclerView(View rootView) {
        final RecyclerView recyclerView = rootView.findViewById(R.id.rv_comments);
        recyclerView.setHasFixedSize(true);
        viewCommentsAdapter = new CommentRecyclerViewAdapter(this);
        recyclerView.setAdapter(viewCommentsAdapter);
    }

    private void downloadComments() {
        SharedPreferences oauthPreference = getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
        String accessToken = oauthPreference.getString(Tags.ACCESS_TOKEN, "");
        Request request = RedditApi.getArticleComments(mPost.subredditName, mPost.redditId, accessToken);
        RedditApi.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "OnFailure: " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws
                    IOException {
                String jsonResp = Objects.requireNonNull(response.body(), "body is null").string();
                Log.d(TAG, "OnResponse, HTTP code: " + response.code());
                Log.d(TAG, jsonResp);

                ArrayList<Comment> arraySubs = JSONUtils.parseJsonComments(jsonResp);
                if(arraySubs != null)
                    runOnUiThread(() -> {
                        viewCommentsAdapter.setData(arraySubs);
                    });
            }
        });
    }

}
