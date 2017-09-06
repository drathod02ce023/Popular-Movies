package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dhaval on 2017/08/31.
 * Provide methods to perform database operations on movie database.
 */
@SuppressWarnings("ConstantConditions")
public class FavMoviesContentProvider extends ContentProvider {

    //Unique identifier for URI (content://com.example.android.popularmovies/movies)
    private static final int CODE_MOVIES = 100;

    //Unique identifier for URI (content://com.example.android.popularmovies/movies/{movieid})
    private static final int CODE_MOVIEID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavMoviesDBHelper favMoviesDBHelper;

    /**
     * @return
     */
    private static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavMoviesContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Sunshine, we use CODE_WEATHER or CODE_WEATHER_WITH_DATE.
         */

        /* This URI is content://com.example.android.popularmovies/movies */
        matcher.addURI(authority, FavMoviesContract.PATH_MOVIES, CODE_MOVIES);

        /*
         * This URI would look something like content://com.example.android.popularmovies/movies/{movieid}
         * The "/#" signifies to the UriMatcher that if CODE_MOVIES is followed by ANY number,
         * that it should return the CODE_MOVIEID code
         */
        matcher.addURI(authority, FavMoviesContract.PATH_MOVIES + "/#", CODE_MOVIEID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        favMoviesDBHelper = new FavMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            //return all the movies
            case CODE_MOVIES: {
                cursor = favMoviesDBHelper.getReadableDatabase().query(
                        FavMoviesContract.FavMoviesEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);

                break;
            }
            //return one movie with specified movie id
            case CODE_MOVIEID: {
                cursor = favMoviesDBHelper.getReadableDatabase().query(
                        FavMoviesContract.FavMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown URI" + uri);
            }
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriCode = sUriMatcher.match(uri);
        long autorowid;
        switch (uriCode) {
            //return one movie with specified movie id
            case CODE_MOVIEID: {
                autorowid = favMoviesDBHelper.getWritableDatabase().insert(FavMoviesContract.FavMoviesEntry.TABLE_NAME, null, values);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown URI" + uri);
            }
        }
        if (autorowid > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            //Toast.makeText(getContext(),"Marked as favourite",Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriCode = sUriMatcher.match(uri);
        int numRowsDeleted;
        switch (uriCode) {/**/
            //return one movie with specified movie id
            case CODE_MOVIEID: {
                numRowsDeleted = favMoviesDBHelper.getWritableDatabase().delete(FavMoviesContract.FavMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown URI" + uri);
            }
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            //Toast.makeText(getContext(),"Marked as Unfavourite",Toast.LENGTH_LONG).show();
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
