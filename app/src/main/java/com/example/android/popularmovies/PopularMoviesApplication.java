package com.example.android.popularmovies;

import android.app.Application;

import com.example.android.popularmovies.log.ReleaseTree;

import timber.log.Timber;

/**
 * Created by dhaval on 2017/09/10.
 */

public class PopularMoviesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree(){

                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
        else{
            Timber.plant(new ReleaseTree());
        }
    }
}
