package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/11.
 * Class to hold the information about the review of the movie.
 */

public class Review {

    @SerializedName("id")
    private String ReviewId;
    @SerializedName("author")
    private String ReviewAuthor;
    @SerializedName("content")
    private String ReviewContent;
    @SerializedName("url")
    private String ReviewUrl;


    public String getReviewId() {
        return ReviewId;
    }


    public String getReviewAuthor() {
        return ReviewAuthor;
    }


    public String getReviewContent() {
        return ReviewContent;
    }


    public String getReviewUrl() {
        return ReviewUrl;
    }
}
