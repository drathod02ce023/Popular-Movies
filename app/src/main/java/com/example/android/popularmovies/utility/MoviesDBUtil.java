package com.example.android.popularmovies.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Video;
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

public class MoviesDBUtil {

    private static final String TAG = MoviesDBUtil.class.getSimpleName();

    //STRING CONSTANTS
    public static final String POPULAR = "popular";
    public static final String TOPRATED = "toprated";

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
     */
    public static Movie getMovieDetails(int movieid,Context context){
        Movie movie;
        String jsonMovie;
        try {
            String urlMovieDetail = ConfigUtil.getProperty(ConfigUtil.url_movie_detail, context);
            String apikey = ConfigUtil.getProperty(ConfigUtil.apikey, context);
            /**
             * Create URL to call multiple APIs (GetDetail,GetReviews,GetVideos) at once using append_to_response method.
             * Example : https://api.themoviedb.org/3/movie/157336?api_key={api_key}&append_to_response=videos,reviews
             */
            Uri uri = Uri.parse(urlMovieDetail.replace("{movie_id}", String.valueOf(movieid))).buildUpon()
                    .appendQueryParameter(PARAM_APIKEY, apikey)
                    .appendQueryParameter(PARAM_APPEND_TO_RESPONSE, VIDEOS + "," + REVIEWS)
                    .build();
            URL url = new URL(uri.toString());
            jsonMovie = getResponseFromURL(url);
            movie = jsonToMovie(jsonMovie);
        }
        catch (IOException ex){
            Log.e(TAG,ex.getMessage());
            throw new RuntimeException(ex);
        }
        return movie;
    }

    /**
     * Get all the movies.
     * @return Get popular movies list.
     */
    public static List<Movie> getPopularMovies(Context context) {
        String jsonMovies;
        List<Movie> lstMovies;
        try
        {
        String apikey = ConfigUtil.getProperty(ConfigUtil.apikey, context);
        String urlPopularMovies = ConfigUtil.getProperty(ConfigUtil.url_popular_movie, context);

        Uri uri = Uri.parse(urlPopularMovies).buildUpon()
                .appendQueryParameter(PARAM_APIKEY, apikey)
                .build();
        URL url = new URL(uri.toString());
        jsonMovies = getResponseFromURL(url);
        lstMovies = jsonToMoviesList(jsonMovies);
        }
        catch (IOException ex){
            Log.e(TAG,ex.getMessage());
            throw new RuntimeException(ex);
        }
        return lstMovies;
    }

    /**
     * Get Top Rated movies
     * @return List of top rated movies came as a result of API call.
     */
    public static List<Movie> getTopRatedMovies(Context context) {
        String jsonMovies;
        List<Movie> lstMovies;
        try {


            String apikey = ConfigUtil.getProperty(ConfigUtil.apikey, context);
            String urlTopratedMovies = ConfigUtil.getProperty(ConfigUtil.url_toprated_movie, context);

            Uri uri = Uri.parse(urlTopratedMovies).buildUpon()
                    .appendQueryParameter(PARAM_APIKEY, apikey)
                    .build();
            URL url = new URL(uri.toString());
            jsonMovies = getResponseFromURL(url);
            lstMovies = jsonToMoviesList(jsonMovies);
        }
        catch (IOException ex){
            Log.e(TAG,ex.getMessage());
            throw new RuntimeException(ex);
        }
        return lstMovies;
    }

    /**
     * Get full poster url by adding BaseURL + posterpath
     * @param posterPath relative path of the movie poster.
     * @return full path of the movie poster.
     */
    public static String getPosterURL(String posterPath,Context context) {
        String urlImage;
        try {
             urlImage = ConfigUtil.getProperty(ConfigUtil.url_base_image, context);
        }
        catch (IOException ex){
            Log.e(TAG,ex.getMessage());
            throw new RuntimeException(ex);
        }
        return urlImage + posterPath;
    }

    //*********************************************** Private Methods *****************************************************

    /**
     * Convert json data to the list of movies using Gson.
     * @param jsondata Json string came as a APIs response.
     */
    private static List<Movie> jsonToMoviesList(String jsondata){
        Log.i(TAG,jsondata);
        List<Movie> lstMovies;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>(){}.getType();
        JsonElement jelement = new JsonParser().parse(jsondata);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray array = jobject.getAsJsonArray("results");
        lstMovies = gson.fromJson(array, listType);
        return lstMovies;
    }

    /**
     * Convert jsondata to Movie object.
     * @param jsondata Json string came as a APIs response.
     * @return Movie object converted from jsondata.
     */
    private static Movie jsonToMovie(String jsondata){
        Log.i(TAG,jsondata);
        Gson gson = new Gson();
        Movie movie;
        List<Review> lstReview;
        List<Video> lstVideo;

        movie = gson.fromJson(jsondata,Movie.class);
        JsonElement jelement = new JsonParser().parse(jsondata);
        JsonObject jobject = jelement.getAsJsonObject();

        //Fetch Reviews
        Type listReviewType = new TypeToken<List<Review>>(){}.getType();
        JsonObject reviewObject = jobject.getAsJsonObject("reviews");
        JsonArray resultArray1 = reviewObject.getAsJsonArray("results");
        lstReview = gson.fromJson(resultArray1,listReviewType);

        //Fetch Videos
        Type listVideoType = new TypeToken<List<Video>>(){}.getType();
        JsonObject movieObject = jobject.getAsJsonObject("videos");
        JsonArray resultArray2 = movieObject.getAsJsonArray("results");
        lstVideo = gson.fromJson(resultArray2,listVideoType);

        movie.setLstReview(lstReview);
        movie.setLstVideo(lstVideo);
        return movie;
    }

    /**
     * Get Response from url in JSON.
     * @param url API url
     * @return response of the API call
     */
    private static String getResponseFromURL(URL url)  {
        String response;
        HttpsURLConnection connection = null;
        InputStream is;
        try {
            connection = (HttpsURLConnection) url.openConnection();
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
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
        finally{
            if(connection != null){
                connection.disconnect();
            }
        }

    }
}
