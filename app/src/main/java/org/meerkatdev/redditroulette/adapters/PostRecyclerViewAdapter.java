package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.SubredditsActivity;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.adapters.viewholders.SubredditViewHolder;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.data.Subreddit;

import java.util.List;

public class PostRecyclerViewAdapter
        extends RecyclerView.Adapter<PostViewHolder> {

    private List<Post> mValues;
    private PostsListActivity mParentActivity;

    private final View.OnClickListener mOnClickListener = view ->
            onClickExt((Post) view.getTag(), view.getContext());

    private void onClickExt(Post item, Context context) {

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_list_content, parent, false);
        return new PostViewHolder(view);
    }


    public PostRecyclerViewAdapter(PostsListActivity parent,
                                        List<Post> items) {
        mValues = items;
        mParentActivity = parent;
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}


