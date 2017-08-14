package com.example.android.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adaptors.MoviesAdaptor;
import com.example.android.popularmovies.asynctasks.AsyncMoviesLoader;
import com.example.android.popularmovies.interfaces.OnAsyncMoviesLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;
import com.example.android.popularmovies.utility.SystemUtil;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesAdaptor.MovieOnClickListener,OnAsyncMoviesLoadCompleted {

    private static final String TAG = MoviesActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MoviesAdaptor mMoviesAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setTitle(R.string.movie_list_title);
        int numberOfGridColumns;


        /**
         * This value decides how many columns should be displayed in the RecyclerView's Grid.
         */
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

             numberOfGridColumns = 2;

        }
        else{
             numberOfGridColumns = 4;

        }

        /**
         * Create RecyclerView and set its Layout to GridView using LayoutManager.
         */
        GridLayoutManager glm = new GridLayoutManager(this,numberOfGridColumns);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setHasFixedSize(false);

        /**
         * Create error message textview to show any error occurred during the course of our action.
         */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /**
         * Set an adaptor to our RecyclerView,Adaptor is the responsible to load the data to the view.
         */
        mMoviesAdapter = new MoviesAdaptor(MoviesActivity.this,this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        /**
         * Create ProgressBar Object, This ProgressBar will be shown when Data is being fetched from network
         * once data fetch is completed, ProgressBar will be invisible.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /**
         * Initial load
         */
        loadPopularMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Load Setting menu
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sortPopular:
                loadPopularMovies();
                break;
            case R.id.sortRating:
                loadTopRatedMovies();
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load Popular movies from DataSource using API Call
     */
    private void loadPopularMovies(){
        if(SystemUtil.isOnline(this)){
            String error = getString(R.string.newtork_error_message);
            showErrorMessage(error);
            return;
        }
        new AsyncMoviesLoader(getApplicationContext(),this).execute(MoviesDBUtil.POPULAR);
    }

    /**
     * Load TopRated movies from DataSource using API Call
     */
    private void loadTopRatedMovies(){
        if(SystemUtil.isOnline(this)){
            String error = getString(R.string.newtork_error_message);
            showErrorMessage(error);
            return;
        }
        new AsyncMoviesLoader(getApplicationContext(),this).execute(MoviesDBUtil.TOPRATED);
    }

    /**
     * Hide Error Message and set RecyclerView to visible.
     */
    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Hide RecyclerView and set Error Message view to visible.
     */
    private void showErrorMessage(String errMsg) {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setText(errMsg);
    }

    @Override
    public void onMovieClickEvent(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getMovieID());
        bundle.putString("poster_path", movie.getPosterPath());
        bundle.putString("original_title", movie.getOriginalTitle());
        bundle.putString("overview", movie.getPlotSynopsis());
        bundle.putString("vote_average", movie.getUserRatings());
        bundle.putString("release_date", movie.getReleaseDate());
        //bundle.putInt("runtime",movie.getRunTime());
        intent.putExtras(bundle);
        startActivity(intent);

    }

    //Before Movies Loading Starts
    @Override
    public void beforeProcessStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    //After Movies Loading Finished
    @Override
    public void afterProcessEnd(List<Movie> lstMovies) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (lstMovies != null) {
            showMoviesDataView();
            mMoviesAdapter.setData(lstMovies);
        } else {
            showErrorMessage(getString(R.string.error_message));
        }
    }
}
