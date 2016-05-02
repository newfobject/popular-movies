package com.newfobject.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Review implements Parcelable {

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    public Review() {
    }


    private Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    public static class Respond {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("page")
        @Expose
        private int page;
        @SerializedName("results")
        @Expose
        private List<Review> reviews = new ArrayList<>();
        @SerializedName("total_pages")
        @Expose
        private int totalPages;
        @SerializedName("total_results")
        @Expose
        private int totalReviews;

        /**
         * @return The id
         */
        public int getId() {
            return id;
        }

        /**
         * @return The page
         */
        public int getPage() {
            return page;
        }

        /**
         * @return The results
         */
        public List<Review> getReviews() {
            return reviews;
        }

        /**
         * @return The totalPages
         */
        public int getTotalPages() {
            return totalPages;
        }

        /**
         * @return The totalResults
         */
        public int getTotalReviews() {
            return totalReviews;
        }
    }
}
