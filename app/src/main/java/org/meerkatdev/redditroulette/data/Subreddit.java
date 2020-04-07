package org.meerkatdev.redditroulette.data;

import org.jetbrains.annotations.NotNull;

public class Subreddit {

    public final String redditId;
    public final String name;
    final String description;
    public final String iconImg;
    final String headerImg;
    final boolean sfw;

    public Subreddit(String redditId, String name, String desc, String icon, String header, boolean over18) {
        this.redditId = redditId;
        this.name = name;
        this.description = desc;
        this.iconImg = icon;
        this.headerImg = header;
        this.sfw = !over18; // NEGATION IMPORTANT
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
