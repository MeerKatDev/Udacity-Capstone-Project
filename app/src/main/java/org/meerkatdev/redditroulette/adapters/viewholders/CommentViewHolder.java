package org.meerkatdev.redditroulette.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public final TextView mAuthorView;
    public final TextView mContentView;

    public CommentViewHolder(View view) {
        super(view);
        mContentView = view.findViewById(R.id.tv_comment_content);
        mAuthorView = view.findViewById(R.id.tv_comment_author);
    }
}
