package org.meerkatdev.redditroulette.data;

import org.jetbrains.annotations.NotNull;

public class Comment {

    public String author;
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
