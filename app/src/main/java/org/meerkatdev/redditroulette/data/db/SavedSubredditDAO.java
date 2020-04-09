package org.meerkatdev.redditroulette.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.meerkatdev.redditroulette.data.SavedSubreddit;
import org.meerkatdev.redditroulette.data.Subreddit;

import java.util.List;

@Dao
public interface SavedSubredditDAO {

    @Query("SELECT * FROM saved_subreddits")
    LiveData<List<Subreddit>> loadAll();

    @Query("SELECT * FROM saved_subreddits WHERE name = :name")
    Subreddit getByName(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subreddit element);

    @Delete
    void delete(Subreddit element);
}
