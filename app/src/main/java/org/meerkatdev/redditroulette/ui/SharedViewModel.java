package org.meerkatdev.redditroulette.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.meerkatdev.redditroulette.data.Subreddit;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Subreddit>> storedSubreddits = new MutableLiveData<>();
    private final MutableLiveData<Subreddit> selectedSubreddit = new MutableLiveData<>();

    public void saveSubreddits(ArrayList<Subreddit> items) {
        storedSubreddits.setValue(items);
    }

    public void selectSubreddit(Subreddit item) {
        selectedSubreddit.setValue(item);
    }

    public LiveData<Subreddit> getSelectedSubreddit() {
        return selectedSubreddit;
    }

    public LiveData<ArrayList<Subreddit>> getStoredSubreddits() {
        return storedSubreddits;
    }
}
