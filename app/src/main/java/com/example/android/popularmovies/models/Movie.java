package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie class is being used to hold the individual movie detail.
 */
public class Movie implements Parcelable {


    private Movie(Parcel in) {
        MovieID = in.readInt();
        RunTime = in.readInt();
        OriginalTitle = in.readString();
        PlotSynopsis = in.readString();
        PosterPath = in.readString();
        ReleaseDate = in.readString();
        UserRatings = in.readString();
        //lstVideo = in.readArrayList(new ArrayList<Video>().getClass().getClassLoader());
        //lstReview = in.readArrayList(new ArrayList<Review>().getClass().getClassLoader());
    }

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

    private List<Review> lstReview;

    private List<Video> lstVideo;

    public List<Review> getLstReview() {
        return lstReview;
    }

    public void setLstReview(List<Review> lstReview) {
        this.lstReview = lstReview;
    }

    public List<Video> getLstVideo() {
        return lstVideo;
    }

    public void setLstVideo(List<Video> lstVideo) {
        this.lstVideo = lstVideo;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getMovieID());
        out.writeInt(getRunTime());
        out.writeString(getOriginalTitle());
        out.writeString(getPlotSynopsis());
        out.writeString(getPosterPath());
        out.writeString(getReleaseDate());
        out.writeString(getUserRatings());
        //out.writeList(getLstVideo());
        //out.writeList(getLstReview());

    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<Movie> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
