package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/11.
 * Class to hold the information about the video of the movie.
 */

public class Video {

    @SerializedName("id")
    private String VideoId;

    @SerializedName("iso_639_1")
    private String ISO_639_1;

    @SerializedName("iso_3166_1")
    private String ISO_3166_1;

    @SerializedName("key")
    private String VideoKey;

    @SerializedName("name")
    private String VideoName;

    @SerializedName("site")
    private String VideoSite;

    @SerializedName("size")
    private String VideoSize;

    @SerializedName("type")
    private String VideoType;


    public String getVideoId() {
        return VideoId;
    }


    public String getISO_639_1() {
        return ISO_639_1;
    }


    public String getISO_3166_1() {
        return ISO_3166_1;
    }


    public String getVideoKey() {
        return VideoKey;
    }


    public String getVideoName() {
        return VideoName;
    }


    public String getVideoSite() {
        return VideoSite;
    }


    public String getVideoSize() {
        return VideoSize;
    }


    public String getVideoType() {
        return VideoType;
    }
}
