package org.meerkatdev.redditroulette.data;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Subreddit {

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
