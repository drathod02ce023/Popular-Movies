package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/10.
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

    public int getMovieID() {
        return MovieID;
    }

    public void setMovieID(int movieID) {
        MovieID = movieID;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public String getOriginalTitle() {
        return OriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        OriginalTitle = originalTitle;
    }

    public String getPlotSynopsis() {
        return PlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        PlotSynopsis = plotSynopsis;
    }

    public String getUserRatings() {
        return UserRatings;
    }

    public void setUserRatings(String userRatings) {
        UserRatings = userRatings;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "OriginalTitle : " +getOriginalTitle();
    }
}
