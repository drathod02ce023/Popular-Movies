package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dhaval on 2017/08/31.
 * Contract information of the favourite movies database
 */

public class FavMoviesContract {


    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIES = "movies";
    public static final String PATH_MOVIEID = "movieid";

    /* Inner class that defines the table contents of the favMovies table */
    public static final class FavMoviesEntry implements BaseColumns {

        //Build content URI to access database.
        //content://com.example.android.popularmovies/movies
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static Uri buildUriWithMovieId(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }

        public static final String TABLE_NAME = "favMovies";

        //COLUMNS
        public static final String COL_MOVIEID = "movieid";
        public static final String COL_RUNTIME = "runtime";
        public static final String COL_TITLE = "title";
        public static final String COL_PLOTSYNOPSIS = "synopsis";
        public static final String COL_RELEASEDATE = "releasedate";
        public static final String COL_USERRATINGS = "userratings";
        public static final String COL_POSTERPATH = "posterpath";
        public static final String COL_POSTER = "poster";

        //COLUMN INDEX (See the column position in dbhelper class)
        public static final int INDEX_MOVIEID = 1;
        public static final int INDEX_POSTERPATH = 7;


    }

}
