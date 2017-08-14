package com.example.android.popularmovies.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.interfaces.OnAsyncDetailsLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;

public class AsyncMovieDetailsLoader extends AsyncTask<String,Void,Movie>{

    private OnAsyncDetailsLoadCompleted onAsyncDetailsLoadCompleted;
    private Context context ;

    public  AsyncMovieDetailsLoader(Context context, OnAsyncDetailsLoadCompleted onAsyncDetailsLoadCompleted){
        this.context = context;
        this.onAsyncDetailsLoadCompleted = onAsyncDetailsLoadCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onAsyncDetailsLoadCompleted.beforeProcessStart();
    }

    @Override
    protected Movie doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        Movie movie;
        String movieid = params[0];
        movie = MoviesDBUtil.getMovieDetails(Integer.valueOf(movieid),context);
        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        onAsyncDetailsLoadCompleted.afterProcessEnd(movie);
    }
}