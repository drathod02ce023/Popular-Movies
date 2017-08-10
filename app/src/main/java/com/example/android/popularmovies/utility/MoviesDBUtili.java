package com.example.android.popularmovies.utility;

import android.net.Uri;

import com.example.android.popularmovies.models.Movie;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhaval on 2017/08/09.
 */

public class MoviesDBUtili {

    private static final String TAG = MoviesDBUtili.class.getSimpleName();

    //STRING CONSTANTS
    public static final String POPULAR = "popular";
    public static final String TOPRATED = "toprated";

    //URL
    private static final String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String TOPRATED_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated";

    //API Key
    private static final String APIKEY = "2072aa286a42827354ab00a6d0fd73fc";

    //PARAM
    private static final String PARAM_APIKEY = "api_key";

    /**
     * Get all the movies.
     * @return
     */
    public static List<Movie> GetPopularMovies() throws IOException {
        String jsonMovies = null;
        List<Movie> lstMovies = null;
        Uri uri = Uri.parse(POPULAR_MOVIES_URL).buildUpon()
                .appendQueryParameter(PARAM_APIKEY,APIKEY)
                .build();
        URL url = new URL(uri.toString());
        jsonMovies = GetResponseFromURL(url);
        lstMovies = jsonToMoviesList(jsonMovies);
        return lstMovies;
    }

    /**
     * Convert json data to the list of movies using Gson.
     * @param jsondata
     */
    public static List<Movie> jsonToMoviesList(String jsondata){
        List<Movie> lstMovies = null;
        Gson gson = new Gson();
        lstMovies = Arrays.asList(gson.fromJson(jsondata, Movie[].class));
        return lstMovies;
    }

    /**
     * Get movies sorted by @param sortby
     * @param sortby
     * @return
     */
    public static List<Movie> GetTopRatedMovies(String sortby) throws IOException {
        String jsonMovies = null;
        List<Movie> lstMovies = null;
        Uri uri = Uri.parse(TOPRATED_MOVIES_URL).buildUpon()
                .appendQueryParameter(PARAM_APIKEY,APIKEY)
                .build();
        URL url = new URL(uri.toString());
        jsonMovies = GetResponseFromURL(url);
        lstMovies = jsonToMoviesList(jsonMovies);
        return lstMovies;
    }

    /**
     * Get Response from url in JSON.
     * @param url
     * @return
     */
    public static String GetResponseFromURL(URL url) throws IOException {
        String response = null;
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream is;
        try {
            is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally{
            connection.disconnect();
        }

    }
}
