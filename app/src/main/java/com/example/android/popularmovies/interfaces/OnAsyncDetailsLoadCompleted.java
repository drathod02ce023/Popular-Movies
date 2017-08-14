package com.example.android.popularmovies.interfaces;

import com.example.android.popularmovies.models.Movie;

/**
 * Created by dhaval on 2017/08/14.
 * Interfacd to do the required process before aysnc task start and after task ends.
 */

public interface OnAsyncDetailsLoadCompleted {
    public void beforeProcessStart();
    public void afterProcessEnd(Movie movie);
}
