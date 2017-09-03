package com.example.android.popularmovies.utility;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dhaval on 2017/08/13.
 * Class to read configuration and properties
 */

 class ConfigUtil {

    public static final String url_popular_movie = "url_popular_movie";
    public static final String url_toprated_movie = "url_toprated_movie";
    public static final String url_base_image = "url_base_image";
    public static final String url_movie_detail = "url_movie_detail";
    public static final String apikey = "apikey";

    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("app.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
