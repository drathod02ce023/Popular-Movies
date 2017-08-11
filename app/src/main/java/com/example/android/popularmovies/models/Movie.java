package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie class is being used to hold the individual movie detail.
 */
public class Movie {

    private List<Video> lstVideo;

    private List<Review> lstReview;

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

// --Commented out by Inspection START (2017/08/11 16:53):
//    public void setMovieID(int movieID) {
//        MovieID = movieID;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:53)

    public String getPosterPath() {
        return PosterPath;
    }

// --Commented out by Inspection START (2017/08/11 16:53):
//    public void setPosterPath(String posterPath) {
//        PosterPath = posterPath;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:53)

    public String getOriginalTitle() {
        return OriginalTitle;
    }

// --Commented out by Inspection START (2017/08/11 16:53):
//    public void setOriginalTitle(String originalTitle) {
//        OriginalTitle = originalTitle;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:53)

    public String getPlotSynopsis() {
        return PlotSynopsis;
    }

// --Commented out by Inspection START (2017/08/11 16:53):
//    public void setPlotSynopsis(String plotSynopsis) {
//        PlotSynopsis = plotSynopsis;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:53)

    public String getUserRatings() {
        return UserRatings;
    }

// --Commented out by Inspection START (2017/08/11 16:54):
//    public void setUserRatings(String userRatings) {
//        UserRatings = userRatings;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:54)

    public String getReleaseDate() {
        return ReleaseDate;
    }

// --Commented out by Inspection START (2017/08/11 16:54):
// --Commented out by Inspection START (2017/08/11 16:54):
// --Commented out by Inspection START (2017/08/11 16:54):
//////    public void setReleaseDate(String releaseDate) {
//////        ReleaseDate = releaseDate;
//////    }
////// --Commented out by Inspection STOP (2017/08/11 16:54)
////    public int getRunTime() {
////        return RunTime;
////    }
//// --Commented out by Inspection STOP (2017/08/11 16:54)
//    public void setRunTime(int runTime) {
//        RunTime = runTime;
//    }
// --Commented out by Inspection STOP (2017/08/11 16:54)

    @Override
    public String toString() {
        return "OriginalTitle : " +getOriginalTitle();
    }
}
