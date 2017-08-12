package com.example.android.popularmovies.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dhaval on 2017/08/12.
 */

public class SystemUtil {

    /**
     * Check if device is connected to network.
     * @return true if connected to network otherwise return false.
     */
    public static  boolean isOnline(Context context){
        //Activity activity = new Activity();
        boolean isConnected;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
