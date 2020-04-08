package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.PostViewActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.CommentViewHolder;
import org.meerkatdev.redditroulette.data.Comment;

import java.util.List;

public class CommentRecyclerViewAdapter
        extends RecyclerView.Adapter<CommentViewHolder> implements RVAdapter<Comment> {

    private List<Comment> mValues;
    private PostViewActivity mParentActivity;
    int noComments;

    private final View.OnLongClickListener mOnClickListener = view ->
            onClickExt((Comment) view.getTag(), view.getContext());

    private boolean onClickExt(Comment item, Context context) {
        // TODO where to?
        String url = "https://www.reddit.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
        return true;
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
        holder.itemView.setOnLongClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return noComments;
    }
}


