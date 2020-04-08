package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.PostViewActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.CommentViewHolder;
import org.meerkatdev.redditroulette.data.Comment;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.utils.Tags;

import java.util.List;

public class CommentRecyclerViewAdapter
        extends RecyclerView.Adapter<CommentViewHolder> {

    private List<Comment> mValues;
    private PostViewActivity mParentActivity;
    int noComments;

    private final View.OnClickListener mOnClickListener = view ->
            onClickExt((Post) view.getTag(), view.getContext());

    private void onClickExt(Post item, Context context) {
    // TODO where to?

    //        Intent intent = new Intent(context, PostViewActivity.class);
    //        // Sending info about subreddit
    //        intent.putExtra(Tags.POST, item);
    //        context.startActivity(intent);
    }


    public void setData(List<Comment> _elements) {
        mValues = _elements;
        noComments = _elements.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comments_list, parent, false);
        return new CommentViewHolder(view);
    }


    public CommentRecyclerViewAdapter(PostViewActivity parent){
        mParentActivity = parent;
        noComments = 0;
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mValues.get(position);
        holder.mAuthorView.setText(comment.author);
        holder.mContentView.setText(comment.content);
        holder.itemView.setTag(comment);
        //holder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return noComments;
    }
}


