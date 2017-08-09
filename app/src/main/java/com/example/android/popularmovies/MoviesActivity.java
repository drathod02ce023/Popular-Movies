package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
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
     * Load Popular movies from Datasource
     */
    private void loadPopularMovies(){

    }

    /**
     * Load TopRated movies from Datasource
     */
    private void loadTopRatedMovies(){

    }

    //private class AsyncLoadMovies extends
}
