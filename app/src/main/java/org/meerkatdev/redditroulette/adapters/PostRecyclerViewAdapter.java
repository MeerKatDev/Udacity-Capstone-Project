package org.meerkatdev.redditroulette.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.meerkatdev.redditroulette.PostViewActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.utils.Tags;

import java.util.List;

public class PostRecyclerViewAdapter
        extends RecyclerView.Adapter<PostViewHolder> implements RVAdapter<Post> {

    private List<Post> mValues;
    private Activity mParentActivity;
    private boolean mTwoPane;
    int noPosts;

    private final View.OnClickListener mOnClickListener = view ->
            onClickExt((Post) view.getTag(), view.getContext());

    private void onClickExt(Post item, Context context) {

        if (false) {
        } else {
            Intent intent = new Intent(context, PostViewActivity.class);
            // Sending info about subreddit
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


    public PostRecyclerViewAdapter(Activity parent){
        noPosts = 0;
        mParentActivity = parent;
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post thisPost = mValues.get(position);
        bindViews(holder, thisPost);
        holder.itemView.setTag(thisPost);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    public static void bindViews(@NonNull PostViewHolder holder, Post thisPost) {
        holder.mTitleView.setText(thisPost.title);
        holder.mLinkView.setText(thisPost.link);
        holder.mAuthorView.setText(thisPost.author);
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

