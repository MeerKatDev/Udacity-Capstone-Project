package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.meerkatdev.redditroulette.PostViewActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.data.db.AppDatabase;
import org.meerkatdev.redditroulette.net.RedditApi;
import org.meerkatdev.redditroulette.ui.OfflinePostsViewModel;
import org.meerkatdev.redditroulette.utils.AppExecutors;
import org.meerkatdev.redditroulette.utils.Tags;

import java.util.Arrays;
import java.util.List;

public class PostRecyclerViewAdapter
        extends RecyclerView.Adapter<PostViewHolder> implements RVAdapter<Post> {

    private static final String TAG = PostRecyclerViewAdapter.class.getSimpleName();

    private List<Post> mValues;
    private static FragmentActivity mParentActivity;
    private boolean mTwoPane;
    private AppDatabase mDb;
    private OfflinePostsViewModel viewModel;
    private String[] savedPostIds;
    int noPosts;

    private final View.OnClickListener mOnClickListener = view ->
            onClickExt((Post) view.getTag(), view.getContext());

    private void onClickExt(Post item, Context context) {
        if(!mTwoPane){
            Intent intent = new Intent(context, PostViewActivity.class);
            intent.putExtra(Tags.POST, item);
            context.startActivity(intent);
        }
    }

    public void setData(List<Post> _elements) {
        mValues = _elements;
        noPosts = _elements.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_posts_list, parent, false);
        return new PostViewHolder(view);
    }

    public PostRecyclerViewAdapter(FragmentActivity parent, boolean twoPane){
        mDb = AppDatabase.getInstance(mParentActivity);
        noPosts = 0;
        mParentActivity = parent;
        mTwoPane = twoPane;
        viewModel = ViewModelProviders.of(mParentActivity).get(OfflinePostsViewModel.class);
        viewModel.getSavedPosts().observe(mParentActivity, v -> {
            Log.d(TAG, "updating savedposts");
            AppExecutors.getInstance().diskIO().execute(() ->
                    savedPostIds = mDb.postDao().loadAllSync().stream().map(a -> a.redditId).toArray(String[]::new)
            );
        });
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post thisPost = mValues.get(position);
        bindSingleView(holder, thisPost);

        holder.mContentView.setTag(thisPost);
        holder.mTitleView.setTag(thisPost);

        holder.mContentView.setOnClickListener(mOnClickListener);
        holder.mTitleView.setOnClickListener(mOnClickListener);
        Arrays.sort(savedPostIds);
        Log.d("SavedPosts", "savedPosts size: " + savedPostIds.length + ", contained? " + Arrays.binarySearch(savedPostIds, thisPost.redditId));
        holder.mSwitch.setChecked(Arrays.binarySearch(savedPostIds, thisPost.redditId) > (-1));
        //holder.mSwitch.setChecked(true);
        holder.mSwitch.setOnCheckedChangeListener( (switchView, isChecked) ->
            handleSavePost(isChecked, thisPost)
        );
    }

    private void handleSavePost(boolean isChecked, Post post) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if(isChecked) {
                Log.d(TAG, "Inserting " + post.title);
                viewModel.savePost(post);
                Subreddit sr = mDb.subredditDAO().getByName(post.subredditName);
                mDb.savedSubredditDAO().insert(sr);
            } else {
                Log.d(TAG, "Deleting " + post.title);
                mDb.postDao().delete(post);
                Subreddit sr = mDb.savedSubredditDAO().getByName(post.subredditName);
                mDb.savedSubredditDAO().delete(sr);
            }
        });
    }

    public static void bindSingleView(@NonNull PostViewHolder holder, Post thisPost) {
        holder.mTitleView.setText(thisPost.title);

        holder.mLinkView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(RedditApi.REDDIT_URL + thisPost.link));
            mParentActivity.startActivity(intent);
        });

        String formattedAuthor = mParentActivity.getString(R.string.reddit_user_tag, thisPost.author);
        holder.mAuthorView.setText(formattedAuthor);

        if(thisPost.getHint().equals("image") || thisPost.mediaUrl.endsWith(".jpg")) {
            holder.mContentView.setVisibility(View.GONE);
            holder.mPostImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(thisPost.mediaUrl).into(holder.mPostImageView);
        } else if(thisPost.getHint().equals("link")) {
            holder.mContentView.setText(thisPost.mediaUrl);
        } else {
            holder.mContentView.setText(thisPost.content);
        }
    }

    @Override
    public int getItemCount() {
        return noPosts;
    }
}


