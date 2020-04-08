package org.meerkatdev.redditroulette.data;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class Comment {

    public String author;
    @SerializedName(value = "body")
    public String content;

    public Comment(String author, String content) {
        this.author = author;

        this.content = content;
    }

    @NotNull
    @Override
    public String toString() {
        return "author: " + this.author + ", "
                + "content: " + this.content;
    }
}
