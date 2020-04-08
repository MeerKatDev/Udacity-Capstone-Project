package org.meerkatdev.redditroulette.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

public class Post implements Parcelable {

    public String redditId;
    public String title;
    public String content;
    public String author;
    public String link;
    public String mediaUrl;
    public String subredditName;
    public String hint;
    public boolean sfw;

    public Post(String id, String title, String author, String content, String link, String mediaUrl, String subredditName, boolean over18, String hint) {
        this.redditId = id; // ID36
        this.title = title;
        this.author = "u/" + author;
        this.content = content;
        this.link = link;
        this.mediaUrl = mediaUrl;
        this.subredditName = subredditName;
        this.hint = hint;
        this.sfw = !over18;
    }

    @NotNull
    @Override
    public String toString() {
        return "title: " + this.title + ", "
            + "hint: " + this.hint + ", "
            + "link: " + this.link + ", "
            + "mediaUrl: " + this.mediaUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.redditId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeString(this.link);
        dest.writeString(this.mediaUrl);
        dest.writeString(this.subredditName);
        dest.writeString(this.hint);
        dest.writeByte(this.sfw ? (byte) 1 : (byte) 0);
    }

    protected Post(Parcel in) {
        this.redditId = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.link = in.readString();
        this.mediaUrl = in.readString();
        this.subredditName = in.readString();
        this.hint = in.readString();
        this.sfw = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}