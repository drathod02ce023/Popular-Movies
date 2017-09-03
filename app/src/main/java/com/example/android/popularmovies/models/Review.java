package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/11.
 * Class to hold the information about the review of the movie.
 */

public class Review implements Parcelable {

    private Review(Parcel in) {
        ReviewId = in.readString();
        ReviewAuthor = in.readString();
        ReviewContent = in.readString();
        ReviewUrl = in.readString();
    }

    @SerializedName("id")
    private final String ReviewId;
    @SerializedName("author")
    private final String ReviewAuthor;
    @SerializedName("content")
    private final String ReviewContent;
    @SerializedName("url")
    private final String ReviewUrl;


    private String getReviewId() {
        return ReviewId;
    }


    private String getReviewAuthor() {
        return ReviewAuthor;
    }


    public String getReviewContent() {
        return ReviewContent;
    }


    private String getReviewUrl() {
        return ReviewUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getReviewId());
        out.writeString(getReviewAuthor());
        out.writeString(getReviewContent());
        out.writeString(getReviewUrl());
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<Review> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Review> CREATOR
            = new Parcelable.Creator<Review>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
