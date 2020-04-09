package org.meerkatdev.redditroulette.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import org.meerkatdev.redditroulette.databinding.ItemPostsListBinding;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleView;
    public final TextView mContentView;
    public final ImageView mLinkView;
    public final TextView mAuthorView;
    public final ImageView mPostImageView;
    public final Switch mSwitch;
    private ItemPostsListBinding binding;

    public PostViewHolder(View view) {
        super(view);
        binding = ItemPostsListBinding.bind(view);
        mTitleView = binding.tvPostTitle;
        mContentView = binding.tvPostContent;
        mLinkView = binding.tvPostLink;
        mAuthorView = binding.tvPostAuthor;
        mPostImageView = binding.ivPostImage;
        mSwitch = binding.swSavePost;
    }
}
