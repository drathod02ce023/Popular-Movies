package com.example.android.popularmovies.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.interfaces.OnAsyncMoviesLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;

import java.util.List;

/**
 * Class to load movies Asynchronously ,Without blocking UI Thread.
 */
public class AsyncMoviesLoader extends AsyncTask<String, Void, List<Movie>> {

    private final OnAsyncMoviesLoadCompleted onAsyncMoviesLoadCompleted;
    private final Context context;

    public AsyncMoviesLoader(Context context, OnAsyncMoviesLoadCompleted onAsyncMoviesLoadCompleted) {
        this.context = context;
        this.onAsyncMoviesLoadCompleted = onAsyncMoviesLoadCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onAsyncMoviesLoadCompleted.beforeProcessStart();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        List<Movie> lstMovies;
        switch (params[0]) {
            case MoviesDBUtil.POPULAR:
                lstMovies = MoviesDBUtil.getPopularMovies(context);
                break;
            case MoviesDBUtil.TOPRATED:
                lstMovies = MoviesDBUtil.getTopRatedMovies(context);
                break;
            default:
                return null;
        }
        return lstMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> lstMovies) {
        super.onPostExecute(lstMovies);
        onAsyncMoviesLoadCompleted.afterProcessEnd(lstMovies);
    }
}
