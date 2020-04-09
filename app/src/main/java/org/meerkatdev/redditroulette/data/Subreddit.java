package org.meerkatdev.redditroulette.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "subreddits", indices = {@Index(value = {"redditId"}, unique = true)})
public class Subreddit {

    @PrimaryKey
    @NonNull
    @SerializedName(value = "id")
    public String redditId;
    @SerializedName(value = "display_name")
    public String name;
    public String description;
    @SerializedName(value = "icon_img")
    public String iconImg;
    @SerializedName(value = "header_img")
    public String headerImg;
    @SerializedName(value = "over18")
    public boolean notSfw;

    public Subreddit(@NonNull String redditId, String name, String description, String iconImg, String headerImg, boolean notSfw) {
        this.redditId = redditId;
        this.name = name;
        this.description = description;
        this.iconImg = iconImg;
        this.headerImg = headerImg;
        this.notSfw = notSfw;
    }

    @NotNull
    @Override
    public String toString() {
        return "ID: " + this.redditId + ", "
            + "name: " + this.name + ", "
            + "iconImg: " + this.iconImg + ", "
            + "headerImg: " + this.headerImg
        ;
    }
}
