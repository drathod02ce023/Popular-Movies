package com.example.android.popularmovies.utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * Created by dhaval on 2017/08/31.
 * Utility to communicate with ContentProvider
 */

public class ProviderUtil {

    /**
     *
     * @param context
     * @param uri
     * @param cv
     * @return
     */
    public static Uri insert(Context context, Uri uri, ContentValues cv){
        return context.getContentResolver().insert(uri,cv);
    }

}
