package org.meerkatdev.redditroulette.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.R;

public class SubredditViewHolder extends RecyclerView.ViewHolder {
    public final TextView mIdView;
    public final TextView mContentView;
    public final ImageView mIconView;

    public SubredditViewHolder(View view) {
        super(view);
        mIdView = view.findViewById(R.id.id_text);
        mContentView = view.findViewById(R.id.content);
        mIconView = view.findViewById(R.id.iv_subreddit_icon);
    }
}
