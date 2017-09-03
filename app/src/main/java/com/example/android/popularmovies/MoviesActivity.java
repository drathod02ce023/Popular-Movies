package com.example.android.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.android.popularmovies.data.FavMoviesContract;
import com.example.android.popularmovies.interfaces.OnAsyncMoviesLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;
import com.example.android.popularmovies.utility.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements MoviesAdaptor.MovieOnClickListener,OnAsyncMoviesLoadCompleted, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesActivity.class.getSimpleName();
    private MoviesAdaptor mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private static final int ID_CURSORLOADER_FAVMOVIES = 3017;
    private static int currentSortId = R.id.sortPopular;
    @BindView(R.id.recyclerview_movies)
     RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
     TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setTitle(R.string.movie_list_title);
        //To use @BindView and other annotations.
        ButterKnife.bind(this);
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
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setHasFixedSize(false);


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

        loadMovies(currentSortId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSort",currentSortId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Load Setting menu
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentSortId = item.getItemId();
        loadMovies(currentSortId);
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param id
     */
    private void loadMovies(int id){
        switch (id){
            case R.id.sortPopular:
                loadPopularMovies();
                break;
            case R.id.sortRating:
                loadTopRatedMovies();
                break;
            case R.id.sortFavourite:
                loadFavouriteMovies();
                break;
            default:
                throw new UnsupportedOperationException("Sorting is not defind.");
        }
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
     * Load favourite movies from database through ContentProvider using Cursorloader
     */
    private void loadFavouriteMovies(){
        getSupportLoaderManager().initLoader(ID_CURSORLOADER_FAVMOVIES,null,this);
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
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", movie.getMovieID());
//        intent.putExtras(bundle);
        intent.putExtra("movie",movie);
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

    //Cursor Loader to get favourite movies from database.

    /**
     *
     * @param loaderId
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        switch (loaderId) {

            case ID_CURSORLOADER_FAVMOVIES:
                return new CursorLoader(this, FavMoviesContract.FavMoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Movie> lstMovies = new ArrayList<>();
        Movie movie;
        mLoadingIndicator.setVisibility(View.GONE);
        if(data.getCount() == 0){
            mMoviesAdapter.setData(null);
            return;
        }
        for(int i =0;i<data.getCount();i++){
            if(data.moveToPosition(i)){
                movie = new Movie();
                movie.setMovieID(data.getInt(FavMoviesContract.FavMoviesEntry.INDEX_MOVIEID));
                movie.setPosterPath(data.getString(FavMoviesContract.FavMoviesEntry.INDEX_POSTERPATH));
                //As of now only these two columns are required.
                lstMovies.add(movie);
            }
        }

        if (lstMovies.size()>0) {
            showMoviesDataView();
            mMoviesAdapter.setData(lstMovies);
        } else {
            showErrorMessage(getString(R.string.error_message));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.setData(null);
    }
}
