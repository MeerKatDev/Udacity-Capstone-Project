package org.meerkatdev.redditroulette.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleView;
    public final TextView mContentView;
    public final TextView mLinkView;
    public final TextView mAuthorView;
    public final ImageView mPostImageView;

    public PostViewHolder(View view) {
        super(view);
        mTitleView = view.findViewById(R.id.tv_post_title);
        mContentView = view.findViewById(R.id.tv_post_content);
        mLinkView = view.findViewById(R.id.tv_post_link);
        mAuthorView = view.findViewById(R.id.tv_post_author);
        mPostImageView = view.findViewById(R.id.iv_post_image);
    }
}
