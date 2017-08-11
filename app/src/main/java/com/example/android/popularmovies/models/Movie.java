package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Movie class is being used to hold the individual movie detail.
 */
public class Movie {


    @SerializedName("id")
    private int MovieID;

    @SerializedName("poster_path")
    private String PosterPath;

    @SerializedName("original_title")
    private String OriginalTitle;

    @SerializedName("overview")
    private String PlotSynopsis;

    @SerializedName("vote_average")
    private String UserRatings;

    @SerializedName("release_date")
    private String ReleaseDate;

    @SerializedName("runtime")
    private int RunTime;

    public int getMovieID() {
        return MovieID;
    }


    public String getPosterPath() {
        return PosterPath;
    }


    public String getOriginalTitle() {
        return OriginalTitle;
    }


    public String getPlotSynopsis() {
        return PlotSynopsis;
    }


    public String getUserRatings() {
        return UserRatings;
    }


    public String getReleaseDate() {
        return ReleaseDate;
    }

    public int getRunTime() {
        return RunTime;
    }


    @Override
    public String toString() {
        return "OriginalTitle : " +getOriginalTitle();
    }
}
