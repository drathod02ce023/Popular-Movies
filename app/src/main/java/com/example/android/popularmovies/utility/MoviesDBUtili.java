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
 * Utility class to connect to themoviedb APIs
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
    private static final String MOVIE_DETAIL_URL = "https://api.themoviedb.org/3/movie/{movie_id}";

    //API Key
    private static final String APIKEY = "2072aa286a42827354ab00a6d0fd73fc";

    //PARAM KEYS
    private static final String PARAM_APIKEY = "api_key";
    private static final String PARAM_APPEND_TO_RESPONSE = "append_to_response";
    //PARAM VALUES
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    /**
     * To get movie details.
     * @param movieid Id of the movie we need the details for.
     * @return Object of the movie class.
     * @throws IOException
     */
    public static Movie GetMovieDetails(int movieid) throws IOException{
        Movie movie = null;
        String jsonMovie = null;
        /**
         * Create URL to call multiple APIs (GetDetail,GetReviews,GetVideos) at once using append_to_response method.
         * Example : https://api.themoviedb.org/3/movie/157336?api_key={api_key}&append_to_response=videos,reviews
         */
        Uri uri = Uri.parse(MOVIE_DETAIL_URL.replace("{movie_id}",String.valueOf(movieid))).buildUpon()
                .appendQueryParameter(PARAM_APIKEY,APIKEY)
                .appendQueryParameter(PARAM_APPEND_TO_RESPONSE,VIDEOS+","+REVIEWS)
                .build();
        URL url = new URL(uri.toString());
        jsonMovie = GetResponseFromURL(url);
        movie = jsonToMovie(jsonMovie);
        return movie;
    }

    /**
     * Get all the movies.
     * @return Get popular movies list.
     */
    public static List<Movie> GetPopularMovies() throws IOException {
        String jsonMovies;
        List<Movie> lstMovies;
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
     * @param jsondata Json string came as a APIs response.
     */
    private static List<Movie> jsonToMoviesList(String jsondata){
        Log.i(TAG,jsondata);
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
     * Convert jsondata to Movie object. TODO Will be used in stage2.
     * @param jsondata Json string came as a APIs response.
     * @return Movie object converted from jsondata.
     */
    public static Movie jsonToMovie(String jsondata){
        Log.i(TAG,jsondata);
        Gson gson = new Gson();
        Movie movie;
        movie = gson.fromJson(jsondata,Movie.class);
        //Fetch Reviews
//        Type listReviewType = new TypeToken<List<Review>>(){}.getType();
//        JsonElement jelement = new JsonParser().parse(jsondata);
//        JsonObject jobject = jelement.getAsJsonObject();
//        JsonArray array = jobject.getAsJsonArray("reviews");
        return movie;
    }

    /**
     * Get Top Rated movies
     * @return List of top rated movies came as a result of API call.
     */
    public static List<Movie> GetTopRatedMovies() throws IOException {
        String jsonMovies;
        List<Movie> lstMovies;
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
     * @param posterPath relative path of the movie poster.
     * @return full path of the movie poster.
     */
    public static String GetPosterURL(String posterPath){
        return BASE_IMAGE_URL + posterPath;
    }

    /**
     * Get Response from url in JSON.
     * @param url API url
     * @return response of the API call
     */
    private static String GetResponseFromURL(URL url) throws IOException {
        String response;
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream is;
        try {
            is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                response = scanner.next();
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
