package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.data.FavMoviesContract.FavMoviesEntry;

/**
 * Created by dhaval on 2017/08/31.
 * Database Helper class.
 */

public class FavMoviesDBHelper extends SQLiteOpenHelper {
    /*
      * This is the name of our database. Database names should be descriptive and end with the
      * .db extension.
      */
    public static final String DATABASE_NAME = "movies.db";


    private static final int DATABASE_VERSION = 1;

    public FavMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TABLE =

                "CREATE TABLE " + FavMoviesEntry.TABLE_NAME + " (" +

                        FavMoviesEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavMoviesEntry.COL_MOVIEID       + " INTEGER NOT NULL, "                 +

                        FavMoviesEntry.COL_PLOTSYNOPSIS + " TEXT NOT NULL,"                  +

                        FavMoviesEntry.COL_RELEASEDATE + " TEXT NOT NULL,"                  +
                        FavMoviesEntry.COL_RUNTIME + " INTEGER NOT NULL,"                  +
                        FavMoviesEntry.COL_TITLE + " TEXT NOT NULL,"                  +
                        FavMoviesEntry.COL_USERRATINGS + " TEXT NOT NULL,"                  +
                        FavMoviesEntry.COL_POSTER + " BLOB NOT NULL"                  +
                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
