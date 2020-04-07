package org.meerkatdev.redditroulette.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleView;
    public final TextView mContentView;

    public PostViewHolder(View view) {
        super(view);
        mTitleView = view.findViewById(R.id.tv_post_title);
        mContentView = view.findViewById(R.id.tv_post_content);
    }
}
