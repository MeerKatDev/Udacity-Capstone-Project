package org.meerkatdev.redditroulette.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.meerkatdev.redditroulette.data.Subreddit;

import java.util.List;

@Dao
public interface SubredditDAO {

    @Query("SELECT * FROM subreddits")
    LiveData<List<Subreddit>> loadAll();
    @Query("SELECT * FROM subreddits")
    List<Subreddit> loadAllSync();

    @Query("SELECT * FROM subreddits WHERE name = :name")
    Subreddit getByName(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Subreddit... elements);
}
