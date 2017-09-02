package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.popularmovies.adaptors.MovieDetailAdaptor;
import com.example.android.popularmovies.asynctasks.AsyncMovieDetailsLoader;
import com.example.android.popularmovies.data.FavMoviesContract;
import com.example.android.popularmovies.interfaces.OnAsyncDetailsLoadCompleted;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Video;
import com.example.android.popularmovies.utility.ImageUtil;
import com.example.android.popularmovies.utility.MoviesDBUtil;
import com.example.android.popularmovies.utility.ProviderUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements OnAsyncDetailsLoadCompleted,LoaderManager.LoaderCallbacks<Cursor>,MovieDetailAdaptor.PlayButtonClickListener {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ProgressBar mLoadingIndicator;
    public static final int ID_CURSORLOADER_FAVMOVIES2 = 3018;
    Movie movieDetail;
    MovieDetailAdaptor movieDetailAdaptor;

    @BindView(R.id.rcvTrailers)
    RecyclerView mrcvTrailers;
    @BindView(R.id.detailLayout) LinearLayout layoutDetailLayout;
    @BindView(R.id.error_message_detail) TextView mErrorMessageDisplay;
    @BindView(R.id.movieTitle) TextView txtViewMovieTitle;
    @BindView(R.id.releaseDateDetail) TextView txtViewReleaseDate;
    @BindView(R.id.userRatingsDetail) TextView txtViewUserRating;
    @BindView(R.id.synopsisDetail) TextView txtViewSynopsis;
    @BindView(R.id.durationDetail) TextView txtViewDuration;
    @BindView(R.id.imgPosterDetail) ImageView imgViewMoviePoster;
    @BindView(R.id.btnFavUnFav) ToggleButton btnFavUnFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(R.string.movie_detail_title);
        //To bind @BindView and other annotations.
        ButterKnife.bind(this);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator_detail);

        //RecycleView for trailers
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mrcvTrailers.setLayoutManager(llm);
        mrcvTrailers.setHasFixedSize(false);

        movieDetailAdaptor = new MovieDetailAdaptor(this,this);
        mrcvTrailers.setAdapter(movieDetailAdaptor);

        //Load Movie Detail Information
        LoadMovieDetail(getIntent());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     *
     * @param intent Intent to retrive information passed from the MoviesActivity(Parent Activity).
     */
    private void LoadMovieDetail(Intent intent){
        if(intent == null){
            return;
        }
        Movie movie =  (Movie)intent.getParcelableExtra("movie");
        int movieid = movie.getMovieID();
        new AsyncMovieDetailsLoader(getApplicationContext(),this).execute(String.valueOf(movieid));
    }

    /**
     * Display data on the screen.
     */
    private void setData() {
        String posterPath = movieDetail.getPosterPath();
        String originalTitle = movieDetail.getOriginalTitle();
        String plotSynopsis = movieDetail.getPlotSynopsis();
        String userRating = movieDetail.getUserRatings() + "/10";
        String releaseDate = movieDetail.getReleaseDate();
        String runtime = String.valueOf(movieDetail.getRunTime()) + " mins";

        //Set data
        //Title
        txtViewMovieTitle.setText(originalTitle);
        //ReleaseDate
        txtViewReleaseDate.setText(releaseDate);
        //Poster
        String fullPosterPath = MoviesDBUtil.getPosterURL(posterPath,this);
        Picasso.with(this).load(fullPosterPath).placeholder(R.mipmap.ic_launcher).into(imgViewMoviePoster);
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
            movieDetail = movie;
            showMoviesDataView();
            setData();
            checkIfMovieIsFavorite();
            //Call an Adaptor method to display trailers.
            //movieDetailAdaptor.setData(movieDetail);
            movieDetailAdaptor.setData(movieDetail);

        } else {
            showErrorMessage();
        }
    }

    /**
     *
     */
    private void checkIfMovieIsFavorite(){
        getSupportLoaderManager().initLoader(ID_CURSORLOADER_FAVMOVIES2,null,this);
    }
    /**
     * Mark movie favourite or unfavourite
     * @param view
     */
    public void markFavUnFav(View view) {
        if(btnFavUnFav.isChecked()){
            //Mark As Favourite (Insert record to SQLite database).
            markMovieFavourite();
        }
        else{
            //Mark As UnFavourite (Delete record to SQLite database).
            markMovieUnFavourite();
        }
    }

    /**
     * Mark movie as favourite and insert it to movie database.
     */
    private void markMovieFavourite(){
        ContentValues cv = new ContentValues();
        byte[] poster = ImageUtil.getByteArrayFromImage(((BitmapDrawable)imgViewMoviePoster.getDrawable()).getBitmap());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_MOVIEID,movieDetail.getMovieID());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_PLOTSYNOPSIS,movieDetail.getPlotSynopsis());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_RELEASEDATE,movieDetail.getReleaseDate());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_RUNTIME,movieDetail.getRunTime());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_TITLE,movieDetail.getOriginalTitle());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_USERRATINGS,movieDetail.getUserRatings());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_POSTER,poster);
        cv.put(FavMoviesContract.FavMoviesEntry.COL_POSTERPATH,movieDetail.getPosterPath());
        //Insert favourite movie detail to favMovie database using ContentResolver.
         ProviderUtil.insert(getApplicationContext(),FavMoviesContract.FavMoviesEntry.buildUriWithMovieId(movieDetail.getMovieID()),cv);

    }
    /**
     * Mark movie as Unfavourite and delete it from movie database.
     */
    private void markMovieUnFavourite(){
        Uri uri = FavMoviesContract.FavMoviesEntry.buildUriWithMovieId(movieDetail.getMovieID());
        String selection = FavMoviesContract.FavMoviesEntry.COL_MOVIEID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(movieDetail.getMovieID())};
        ProviderUtil.delete(getApplicationContext(),uri,selection,selectionArgs);
    }

    //Cursor Loader to check if movie is in the favorite list.
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

            case ID_CURSORLOADER_FAVMOVIES2:
                String selection = FavMoviesContract.FavMoviesEntry.COL_MOVIEID + " = ?";
                String[] selectionArgs = new String[]{Integer.toString(movieDetail.getMovieID())};
                return new CursorLoader(this, FavMoviesContract.FavMoviesEntry.buildUriWithMovieId(movieDetail.getMovieID()),
                        null,
                        selection,
                        selectionArgs,
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
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data.getCount() == 0){
            //Movie is not favorite (change button state to "Mark as favorite")
            btnFavUnFav.setChecked(false);
        }
        else{
            //Movie is favorite (change button state to "mark as unfavorite")
            btnFavUnFav.setChecked(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onPlayButtonClick(Video video) {
        //Toast.makeText(this,video.getVideoName(),Toast.LENGTH_LONG).show();
        // your video id
        // for instance a video link: http://www.youtube.com/watch?v=58f1430ac3a3681a52004703
        //String videoId = video.getVideoId();
        String videoId = "twZggnNbFqo";
        //Toast.makeText(this,video.getVideoId(),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }
}
