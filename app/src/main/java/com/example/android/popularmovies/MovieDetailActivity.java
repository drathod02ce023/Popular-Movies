package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtili;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private LinearLayout layoutDetailLayout;
    private TextView txtViewMovieTitle;
    private TextView txtViewReleaseDate;
    private TextView txtViewUserRating;
    private TextView txtViewSynopsis;
    private TextView txtViewDuration;
    private ImageView txtViewMoviePoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(R.string.movie_detail_title);
        txtViewMovieTitle = (TextView)findViewById(R.id.movieTitle);
        txtViewMoviePoster = (ImageView)findViewById(R.id.imgPosterDetail);
        txtViewReleaseDate = (TextView)findViewById(R.id.releaseDateDetail);
        txtViewUserRating = (TextView)findViewById(R.id.userRatingsDetail);
        txtViewSynopsis = (TextView)findViewById(R.id.synopsisDetail);
        txtViewDuration = (TextView)findViewById(R.id.durationDetail);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator_detail);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_detail);
        layoutDetailLayout = (LinearLayout)findViewById(R.id.detailLayout);

        //Load Movie Detail Information
        LoadMovieDetail(getIntent());

    }

    /**
     *
     * @param intent Intent to retrive information passed from the MoviesActivity(Parent Activity).
     */
    private void LoadMovieDetail(Intent intent){
        if(intent == null){
            return;
        }
        Bundle bundle = intent.getExtras();
        int movieid = bundle.getInt("id");
        new AsyncLoadMovieDetail().execute(String.valueOf(movieid));
    }

    /**
     * Display data on the screen.
     */
    private void setData(Movie movie){
        String posterPath = movie.getPosterPath();
        String originalTitle = movie.getOriginalTitle();
        String plotSynopsis = movie.getPlotSynopsis();
        String userRating = movie.getUserRatings() + "/10";
        String releaseDate = movie.getReleaseDate();
        String runtime = String.valueOf(movie.getRunTime()) + " mins";

        //Set data
        //Title
        txtViewMovieTitle.setText(originalTitle);
        //ReleaseDate
        txtViewReleaseDate.setText(releaseDate);
        //Poster
        String fullPosterPath = MoviesDBUtili.GetPosterURL(posterPath);
        Picasso.with(this).load(fullPosterPath).into(txtViewMoviePoster);
        //Duration
        txtViewDuration.setText(runtime);
        //UserRatings
        txtViewUserRating.setText(userRating);
        //Synopsis
        txtViewSynopsis.setText(plotSynopsis);
    }
    /**
     * Hide Error Message and set RecyclerView to visible.
     */
    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        layoutDetailLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide RecyclerView and set Error Message view to visible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        layoutDetailLayout.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class AsyncLoadMovieDetail extends AsyncTask<String,Void,Movie>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            Movie movie = null;
            String movieid = params[0];
            try {
                movie = MoviesDBUtili.GetMovieDetails(Integer.valueOf(movieid));
            } catch (IOException e) {
                Log.e(TAG,e.getMessage());
            }
            catch(Exception e){
                Log.e(TAG,e.getMessage());
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movie != null) {
                showMoviesDataView();
                setData(movie);
            } else {
                showErrorMessage();
            }
        }
    }
}
