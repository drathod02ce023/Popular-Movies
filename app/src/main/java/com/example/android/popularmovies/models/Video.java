package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 2017/08/11.
 * Class to hold the information about the video of the movie.
 */

public class Video {

    private String VideoId;
    private String ISO_639_1;
    private String ISO_3166_1;
    private String VideoKey;
    private String VideoName;
    private String VideoSite;
    private String VideoSize;
    private String VideoType;

    @SerializedName("id")
    public String getVideoId() {
        return VideoId;
    }

    @SerializedName("iso_639_1")
    public String getISO_639_1() {
        return ISO_639_1;
    }

    @SerializedName("iso_3166_1")
    public String getISO_3166_1() {
        return ISO_3166_1;
    }

    @SerializedName("key")
    public String getVideoKey() {
        return VideoKey;
    }

    @SerializedName("name")
    public String getVideoName() {
        return VideoName;
    }

    @SerializedName("site")
    public String getVideoSite() {
        return VideoSite;
    }

    @SerializedName("size")
    public String getVideoSize() {
        return VideoSize;
    }

    @SerializedName("type")
    public String getVideoType() {
        return VideoType;
    }
}
