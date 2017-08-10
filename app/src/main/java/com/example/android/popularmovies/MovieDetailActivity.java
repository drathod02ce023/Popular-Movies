package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utility.MoviesDBUtili;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.R.id.movieTitle;

public class MovieDetailActivity extends AppCompatActivity {

    TextView txtViewMovieTitle;
    TextView txtViewReleaseDate;
    TextView txtViewUserRating;
    TextView txtViewSynopsis;
    TextView txtViewDuration;
    ImageView txtViewMoviePoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(R.string.movie_detail_title);
        txtViewMovieTitle = (TextView)findViewById(movieTitle);
        txtViewMoviePoster = (ImageView)findViewById(R.id.imgPosterDetail);
        txtViewReleaseDate = (TextView)findViewById(R.id.releaseDateDetail);
        txtViewUserRating = (TextView)findViewById(R.id.userRatingsDetail);
        txtViewSynopsis = (TextView)findViewById(R.id.synopsisDetail);
        txtViewDuration = (TextView)findViewById(R.id.durationDetail);
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
        int runtime = bundle.getInt("runtime");

        //Set data
        //Title
        txtViewMovieTitle.setText(originalTitle);
        //ReleaseDate
        txtViewReleaseDate.setText(releaseDate);
        //Poster
        String fullPosterPath = MoviesDBUtili.GetPosterURL(posterPath);
        Picasso.with(this).load(fullPosterPath).into(txtViewMoviePoster);
        //Duration
        txtViewDuration.setText(String.valueOf(runtime));
        //UserRatings
        txtViewUserRating.setText(userRating+"/10");
        //Synopsis
        txtViewSynopsis.setText(plotSynopsis);
    }
}
