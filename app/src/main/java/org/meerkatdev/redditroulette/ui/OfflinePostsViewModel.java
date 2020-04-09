package org.meerkatdev.redditroulette.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.meerkatdev.redditroulette.data.Post;
import org.meerkatdev.redditroulette.data.db.AppDatabase;

import java.util.List;

public class OfflinePostsViewModel extends AndroidViewModel {

    private static final String TAG = OfflinePostsViewModel.class.getSimpleName();

    private LiveData<List<Post>> savedPosts;
    private AppDatabase database;

    public OfflinePostsViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving the movies from the database");
        savedPosts = database.postDao().loadAll();
    }

    public void savePost(Post post) {
        database.postDao().insert(post);
    }

    public LiveData<List<Post>> getSavedPosts() {
        return savedPosts;
    }

}
