package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adaptors.MoviesAdaptor;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtili;

import java.io.IOException;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {

    private static final String TAG = MoviesDBUtili.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MoviesAdaptor mMoviesAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        /**
         * This value decides how many columns should be displayed in the Recyclerview's Grid.
         */
        int numberOfGridColumns = 2;

        /**
         * Create RecyclerView and set its Layout to GridView using LayoutManager.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,numberOfGridColumns));
        mRecyclerView.setHasFixedSize(true);

        /**
         * Create error message textview to show any error occured during the course of our action.
         */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /**
         * Set an adaptor to our RecyclerView,Adaptor is the responsible to load the data to the view.
         */
        mMoviesAdapter = new MoviesAdaptor(MoviesActivity.this);
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
     * Load Popular movies from Datasource using API Call
     */
    private void loadPopularMovies(){
        new AsyncMoviesLoader().execute(MoviesDBUtili.POPULAR);
    }

    /**
     * Load TopRated movies from Datasource using API Call
     */
    private void loadTopRatedMovies(){
        new AsyncMoviesLoader().execute(MoviesDBUtili.TOPRATED);
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
     * Hide RecyclerView and set Error Messge view to visible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Class to load movies Asynchronously ,Without blocking UI Thread.
     */
    private class AsyncMoviesLoader extends AsyncTask<String,Void,List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params)  {
            if (params.length == 0) {
                return null;
            }
            switch (params[0]){
                case MoviesDBUtili.POPULAR:
                    try {
                        MoviesDBUtili.GetPopularMovies();
                    } catch (IOException e) {
                        Log.e(TAG,e.getMessage());
                    }
                    catch(Exception e){
                        Log.e(TAG,e.getMessage());
                    }
                    break;
                case MoviesDBUtili.TOPRATED:
                    try {
                        MoviesDBUtili.GetPopularMovies();
                    } catch (IOException e) {
                        Log.e(TAG,e.getMessage());
                    }
                    catch(Exception e){
                        Log.e(TAG,e.getMessage());
                    }
                    break;
                default:
                    return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> lstMovies) {
            super.onPostExecute(lstMovies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (lstMovies != null) {
                showMoviesDataView();
                mMoviesAdapter.setData(lstMovies);
            } else {
                showErrorMessage();
            }
        }
    }

}
