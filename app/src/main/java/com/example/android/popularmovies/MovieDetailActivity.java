package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
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
import com.example.android.popularmovies.views.StatefulRecyclerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.R.id.btnFavUnFav;

public class MovieDetailActivity extends AppCompatActivity implements OnAsyncDetailsLoadCompleted,
        LoaderManager.LoaderCallbacks<Cursor>, MovieDetailAdaptor.PlayButtonClickListener {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ProgressBar mLoadingIndicator;
    private static final int ID_CURSORLOADER_FAVMOVIES2 = 3018;
    private Movie movieDetail;
    private MovieDetailAdaptor movieDetailAdaptor;
    LinearLayoutManager mRecViewLayoutManager;

    @BindView(R.id.detailCordinator)
    CoordinatorLayout cordinatorLayout;
    @BindView(btnFavUnFav)
    ToggleButton mtbFavUnFav;
    @BindView(R.id.rcvTrailers)
    StatefulRecyclerView mrcvTrailers;
    @BindView(R.id.detailLayout)
    ConstraintLayout layoutDetailLayout;
    @BindView(R.id.error_message_detail)
    TextView mErrorMessageDisplay;
    @BindView(R.id.movieTitle)
    TextView txtViewMovieTitle;
    @BindView(R.id.releaseDateDetail)
    TextView txtViewReleaseDate;
    @BindView(R.id.userRatingsDetail)
    TextView txtViewUserRating;
    @BindView(R.id.synopsisDetail)
    TextView txtViewSynopsis;
    @BindView(R.id.durationDetail)
    TextView txtViewDuration;
    @BindView(R.id.imgPosterDetail)
    ImageView imgViewMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_cordinator);
        setTitle(R.string.movie_detail_title);
        //To bind @BindView and other annotations.
        ButterKnife.bind(this);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator_detail);

        //RecycleView for trailers
        mRecViewLayoutManager = new LinearLayoutManager(this);
        mRecViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrcvTrailers.setLayoutManager(mRecViewLayoutManager);
        mrcvTrailers.setHasFixedSize(false);

        movieDetailAdaptor = new MovieDetailAdaptor(this, this);
        mrcvTrailers.setAdapter(movieDetailAdaptor);
        mtbFavUnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtbFavUnFav.isChecked()) {
                    //Mark As Favourite (Insert record to SQLite database).
                    markMovieFavourite();
                } else {
                    //Mark As UnFavourite (Delete record to SQLite database).
                    markMovieUnFavourite();
                }
            }
        });

        //Load Movie Detail Information
        LoadMovieDetail(getIntent());
    }

    /**
     * @param intent Intent to retrive information passed from the MoviesActivity(Parent Activity).
     */
    private void LoadMovieDetail(Intent intent) {
        if (intent == null) {
            return;
        }
        Movie movie = intent.getParcelableExtra("movie");
        int movieid = movie.getMovieID();
        new AsyncMovieDetailsLoader(getApplicationContext(), this).execute(String.valueOf(movieid));
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
        String fullPosterPath = MoviesDBUtil.getPosterURL(posterPath, this);
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
        mErrorMessageDisplay.setVisibility(View.GONE);
        /* Then, make sure the weather data is visible */
        layoutDetailLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide RecyclerView and set Error Message view to visible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        layoutDetailLayout.setVisibility(View.GONE);
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
            //To maintain scroll position
            mrcvTrailers.restorePosition();

        } else {
            showErrorMessage();
        }
    }

    /**
     *
     */
    private void checkIfMovieIsFavorite() {
        getSupportLoaderManager().initLoader(ID_CURSORLOADER_FAVMOVIES2, null, this);
    }


    /**
     * Mark movie as favourite and insert it to movie database.
     */
    private void markMovieFavourite() {
        ContentValues cv = new ContentValues();
        byte[] poster = ImageUtil.getByteArrayFromImage(((BitmapDrawable) imgViewMoviePoster.getDrawable()).getBitmap());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_MOVIEID, movieDetail.getMovieID());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_PLOTSYNOPSIS, movieDetail.getPlotSynopsis());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_RELEASEDATE, movieDetail.getReleaseDate());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_RUNTIME, movieDetail.getRunTime());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_TITLE, movieDetail.getOriginalTitle());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_USERRATINGS, movieDetail.getUserRatings());
        cv.put(FavMoviesContract.FavMoviesEntry.COL_POSTER, poster);
        cv.put(FavMoviesContract.FavMoviesEntry.COL_POSTERPATH, movieDetail.getPosterPath());
        //Insert favourite movie detail to favMovie database using ContentResolver.
        ProviderUtil.insert(getApplicationContext(), FavMoviesContract.FavMoviesEntry.buildUriWithMovieId(movieDetail.getMovieID()), cv);
        Snackbar snackbar = Snackbar.make(cordinatorLayout, "Movie marked as Favorite.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markMovieUnFavourite();
                        Snackbar snackbar1 = Snackbar.make(cordinatorLayout, "Movie marked as UnFavorite.", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }

    /**
     * Mark movie as Unfavourite and delete it from movie database.
     */
    private void markMovieUnFavourite() {
        Uri uri = FavMoviesContract.FavMoviesEntry.buildUriWithMovieId(movieDetail.getMovieID());
        String selection = FavMoviesContract.FavMoviesEntry.COL_MOVIEID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(movieDetail.getMovieID())};
        ProviderUtil.delete(getApplicationContext(), uri, selection, selectionArgs);
        Snackbar snackbar = Snackbar.make(cordinatorLayout, "Movie marked as UnFavorite.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markMovieFavourite();
                        Snackbar snackbar1 = Snackbar.make(cordinatorLayout, "Movie marked as Favorite.", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }

    //Cursor Loader to check if movie is in the favorite list.

    /**
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
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data.getCount() == 0) {
            //Movie is not favorite (change button state to "Mark as favorite")
            mtbFavUnFav.setChecked(false);
        } else {
            //Movie is favorite (change button state to "mark as unfavorite")
            mtbFavUnFav.setChecked(true);
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
        Intent intent;

        if (isYouTubeInstalled()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        }
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }

    /**
     * Chceck if YouTube is installed
     *
     * @return
     */
    protected boolean isYouTubeInstalled() {
        String packageName = "com.google.android.youtube";
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }
}
