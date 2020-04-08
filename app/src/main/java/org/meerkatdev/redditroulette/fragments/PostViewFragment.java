package org.meerkatdev.redditroulette.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.meerkatdev.redditroulette.R;

// Fragment:
// onCreate (only once): Called to do initial creation of a fragment.
// onCreateView: Called to have the fragment instantiate its user interface view
// onViewCreated
// onViewStateRestored
// onStart: Called when the Fragment is visible to the user.
public class PostViewFragment extends Fragment {


    /*
     * The system calls this when it's time for the fragment to draw its user interface for the first time.
     * To draw a UI for your fragment, you must return a View from this method that is the root of your fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_view, container, false);

        return rootView;
    }
}
