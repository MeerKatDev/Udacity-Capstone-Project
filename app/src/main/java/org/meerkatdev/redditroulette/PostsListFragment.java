package org.meerkatdev.redditroulette;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.meerkatdev.redditroulette.utils.Tags;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link SubredditsActivity}
 * in two-pane mode (on tablets) or a {@link PostsListActivity}
 * on handsets.
 */
public class PostsListFragment extends Fragment {

    public PostsListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Tags.SUBREDDIT_ID)) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(Tags.OAUTH_DATA, Context.MODE_PRIVATE);
            if(sharedPref.contains(Tags.ACCESS_TOKEN)) {

            }
            String mSubredditId = getArguments().getString(Tags.SUBREDDIT_ID);
            String mSubredditName = getArguments().getString(Tags.SUBREDDIT_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("/r/" + mSubredditId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.posts_list_content, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ( (TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
//        }

        return rootView;
    }
}
