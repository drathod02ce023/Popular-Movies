package com.example.android.popularmovies.log;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by dhaval on 2017/09/10.
 * Release Log Mode
 */

public class ReleaseTree extends Timber.Tree {
    @Override
    protected boolean isLoggable(int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }
        return true;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        if(isLoggable(priority)){
            if(priority == Log.ERROR && t != null){
                //Crashlystics.log(t);
                //Use any crash library like Fiber or Crashlystics
            }
        }
        //Write a logic to split the message it its too long to be displayed on the single line.
        //or anyother logic on the message.
        if(priority == Log.ASSERT){
            Log.wtf(tag,message);
        }
        else{
            Log.println(priority,tag,message);
        }
    }
}
