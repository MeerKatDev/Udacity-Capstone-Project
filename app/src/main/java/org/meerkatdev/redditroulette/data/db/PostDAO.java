package org.meerkatdev.redditroulette.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.meerkatdev.redditroulette.data.Post;

import java.util.List;

@Dao
public interface PostDAO {

    @Query("SELECT * FROM posts")
    LiveData<List<Post>> loadAll();

    @Query("SELECT * FROM posts WHERE subredditName = :subredditName")
    LiveData<List<Post>> getAllBySubredditName(String subredditName);

    @Insert
    void insert(Post element);

    @Delete
    void delete(Post element);
}
