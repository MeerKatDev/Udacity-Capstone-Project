package org.meerkatdev.redditroulette.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.redditroulette.PostsListActivity;
import org.meerkatdev.redditroulette.R;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.data.Post;

import java.util.List;

public class PostRecyclerViewAdapter
        extends RecyclerView.Adapter<PostViewHolder> {

    private List<Post> mValues;
    private PostsListActivity mParentActivity;
    int noPosts;

    private final View.OnClickListener mOnClickListener = view ->
            onClickExt((Post) view.getTag(), view.getContext());

    private void onClickExt(Post item, Context context) {

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


    public PostRecyclerViewAdapter(PostsListActivity parent){
                                        //, List<Post> items) {
        noPosts = 0;
        mParentActivity = parent;
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Log.d("rv",  "position: " + position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mLinkView.setText(mValues.get(position).link);
        holder.mAuthorView.setText(mValues.get(position).author);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return noPosts;
    }
}


