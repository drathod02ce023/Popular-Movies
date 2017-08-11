package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/11.
 * Class to hold the information about the review of the movie.
 */

public class Review {

    private String ReviewId;
    private String ReviewAuthor;
    private String ReviewContent;
    private String ReviewUrl;

    @SerializedName("id")
    public String getReviewId() {
        return ReviewId;
    }

    @SerializedName("author")
    public String getReviewAuthor() {
        return ReviewAuthor;
    }

    @SerializedName("content")
    public String getReviewContent() {
        return ReviewContent;
    }

    @SerializedName("url")
    public String getReviewUrl() {
        return ReviewUrl;
    }
}
