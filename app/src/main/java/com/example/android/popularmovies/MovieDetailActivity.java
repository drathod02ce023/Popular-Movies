package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.asynctasks.AsyncMovieDetailsLoader;
import com.example.android.popularmovies.interfaces.OnAsyncDetailsLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements OnAsyncDetailsLoadCompleted {
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
        new AsyncMovieDetailsLoader(getApplicationContext(),this).execute(String.valueOf(movieid));
    }

    /**
     * Display data on the screen.
     */
    private void setData(Movie movie) {
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
        String fullPosterPath = MoviesDBUtil.getPosterURL(posterPath,this);
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


    //Before Movie Detail Loading Starts
    @Override
    public void beforeProcessStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    //After Movie Detail Loading Finished
    @Override
    public void afterProcessEnd(Movie movie) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movie != null) {
            showMoviesDataView();
            setData(movie);
        } else {
            showErrorMessage();
        }
    }
}
