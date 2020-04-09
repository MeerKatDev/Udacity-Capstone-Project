package org.meerkatdev.redditroulette.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "saved_subreddits", inheritSuperIndices=true)
public class SavedSubreddit extends Subreddit {

    public SavedSubreddit(@NonNull String redditId, String name, String description, String iconImg, String headerImg, boolean notSfw) {
        super(redditId, name, description, iconImg, headerImg, notSfw);
    }
}
