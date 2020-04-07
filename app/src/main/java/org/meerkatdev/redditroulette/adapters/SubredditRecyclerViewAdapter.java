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
import org.meerkatdev.redditroulette.PostsListFragment;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsActivity;
import org.meerkatdev.redditroulette.adapters.viewholders.SubredditViewHolder;
import org.meerkatdev.redditroulette.data.Subreddit;
import org.meerkatdev.redditroulette.utils.Tags;

import java.util.List;

public class SubredditRecyclerViewAdapter
        extends RecyclerView.Adapter<SubredditViewHolder> {

    private final SubredditsActivity mParentActivity;
    private final List<Subreddit> mValues;
    private boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = view ->
        onClickExt((Subreddit)view.getTag(), view.getContext());

    private void onClickExt(Subreddit item, Context context) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(Tags.SUBREDDIT_ID, item.redditId);
            arguments.putString(Tags.SUBREDDIT_NAME, item.name);
            PostsListFragment fragment = new PostsListFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(context, PostsListActivity.class);
            // Sending info about subreddit
            intent.putExtra(Tags.SUBREDDIT_ID, item.redditId);
            intent.putExtra(Tags.SUBREDDIT_NAME, item.name);
            context.startActivity(intent);
        }
    }

    public SubredditRecyclerViewAdapter(SubredditsActivity parent,
                                        List<Subreddit> items,
                                        boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NotNull
    @Override
    public SubredditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subreddit_list_content, parent, false);
        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubredditViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).redditId);
        holder.mContentView.setText(mValues.get(position).name);
        String imagePath = mValues.get(position).iconImg;
        if(imagePath != null && !imagePath.isEmpty())
            Picasso.get().load(imagePath).into(holder.mIconView);
        else
            holder.mIconView.setBackgroundResource(R.drawable.ic_reddit_svgrepo_com);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}

