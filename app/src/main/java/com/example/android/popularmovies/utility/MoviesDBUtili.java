package com.example.android.popularmovies.utility;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
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
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

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
        Type listType = new TypeToken<List<Movie>>(){}.getType();
        JsonElement jelement = new JsonParser().parse(jsondata);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray array = jobject.getAsJsonArray("results");
        try{
            lstMovies = gson.fromJson(array, listType);
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
        }
        return lstMovies;
    }

    /**
     * Get Top Rated movies
     * @return
     */
    public static List<Movie> GetTopRatedMovies() throws IOException {
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
     * Get full poster url by adding BaseURL + posterpath
     * @param posterPath
     * @return
     */
    public static String GetPosterURL(String posterPath){
        return BASE_IMAGE_URL + posterPath;
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
                response = scanner.next();
                Log.i(TAG,response);
                return response;
            } else {
                return null;
            }
        }
        finally{
            connection.disconnect();
        }

    }
}
