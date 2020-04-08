package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.SubredditsListActivity;
import org.meerkatdev.redditroulette.fragments.PostsListFragment;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.SubredditViewHolder;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.ui.SharedViewModel;
import org.meerkatdev.redditroulette.utils.Tags;

import java.util.List;

public class SubredditRecyclerViewAdapter
        extends RecyclerView.Adapter<SubredditViewHolder> implements RVAdapter<Subreddit> {

    private final SubredditsListActivity mParentActivity;
    private List<Subreddit> mValues;
    private boolean mTwoPane;
    private final String mAccessToken;
    private int noSubreddits;

    private SharedViewModel sharedViewModel;

    private final View.OnClickListener mOnClickListener = view ->
        onClickExt((Subreddit)view.getTag(), view.getContext());

    private void onClickExt(Subreddit item, Context context) {
        if (mTwoPane) {
            sharedViewModel.selectSubreddit(item);
        } else {
            Intent intent = new Intent(context, PostsListActivity.class);
            // Sending info about subreddit
            intent.putExtra(Tags.SUBREDDIT_NAME, item.name);
            intent.putExtra(Tags.ACCESS_TOKEN, mAccessToken);
            context.startActivity(intent);
        }
    }

    public SubredditRecyclerViewAdapter(SubredditsListActivity parent,
                                        boolean twoPane,
                                        String accessToken) {
        mParentActivity = parent;
        noSubreddits = 0;
        mTwoPane = twoPane;
        mAccessToken = accessToken;
    }

    @Override
    public void setData(List<Subreddit> elements) {
        mValues = elements;
        noSubreddits = elements.size();
        notifyDataSetChanged();
    }

    public void setViewModel(SharedViewModel viewModel) {
        sharedViewModel = viewModel;
    }

    @NotNull
    @Override
    public SubredditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subreddit_list, parent, false);
        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubredditViewHolder holder, int position) {
        Subreddit sr = mValues.get(position);
        String imagePath = sr.iconImg;
        holder.mIdView.setText(sr.redditId);
        holder.mContentView.setText(sr.name);

        if(imagePath != null && !imagePath.isEmpty())
            Picasso.get().load(imagePath).into(holder.mIconView);
        else
            holder.mIconView.setImageResource(R.drawable.ic_reddit_svgrepo_com);

        holder.itemView.setTag(sr);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return noSubreddits;
    }
}

