package org.meerkatdev.redditroulette.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.meerkatdev.redditroulette.adapters.PostRecyclerViewAdapter;
import org.meerkatdev.redditroulette.adapters.viewholders.PostViewHolder;
import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.databinding.FragmentPostViewBinding;
import org.meerkatdev.redditroulette.utils.Tags;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostViewFragment extends Fragment {

    private FragmentPostViewBinding binding;
    private Post mPost;

    /*
     * The system calls this when it's time for the fragment to draw its user interface for the first time.
     * To draw a UI for your fragment, you must return a View from this method that is the root of your fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostViewBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        Intent intent = getActivity().getIntent();
        View itemRootView = binding.itemPostView.getRoot();
        if(intent.getExtras().containsKey(Tags.POST)) {
            mPost = intent.getParcelableExtra(Tags.POST);
            PostViewHolder viewHolder = new PostViewHolder(itemRootView);
            Log.d("TAG", "MPost: " + mPost.toString());
            Log.d("TAG", "itemRootView: " + itemRootView.toString());
            PostRecyclerViewAdapter.bindViews(viewHolder, mPost);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

    }
}
