package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    TextView movieTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(R.string.movie_detail_title);
        movieTitle = (TextView)findViewById(R.id.movieTitle);
        initialize(getIntent());

    }

    private void initialize(Intent intent){
        if(intent == null){
            return;
        }
        Bundle bundle = intent.getExtras();
        int movieid = bundle.getInt("id");
        String posterPath = bundle.getString("poster_path");
        String originalTitle = bundle.getString("original_title");
        String plotSynopsis = bundle.getString("overview");
        String userRating = bundle.getString("vote_average");
        String releaseDate = bundle.getString("release_date");

        //Set data
        movieTitle.setText(originalTitle);
    }
}
