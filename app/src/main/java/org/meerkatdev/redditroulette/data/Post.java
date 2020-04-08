package org.meerkatdev.redditroulette.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Post implements Parcelable {

    @SerializedName(value = "id")
    public String redditId;
    public String title;
    @SerializedName(value = "selftext")
    public String content;
    public String author;
    @SerializedName(value = "permalink")
    public String link;
    @SerializedName(value = "url")
    public String mediaUrl;
    @SerializedName(value = "subreddit")
    public String subredditName;
    @SerializedName(value = "post_hint")
    public String hint;
    @SerializedName(value = "over_18")
    public boolean sfw;

    @NotNull
    @Override
    public String toString() {
        return "title: " + this.title + ", "
            + "hint: " + this.hint + ", "
            + "link: " + this.link + ", "
            + "mediaUrl: " + this.mediaUrl;
    }

    public String getHint() {
        return this.hint==null ? "" :this.hint;
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