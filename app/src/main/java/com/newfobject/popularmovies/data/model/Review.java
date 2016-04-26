package com.newfobject.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Review {

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
