package com.foodattack.foodattack;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by Xue Hui on 5/7/2015.
 */
public class ParseApplication extends Application {

    private final String applicationID = "4sWMVHznnjN5mmMwwHe4tFiMbv2lpvPTpl3HGzil";
    private final String clientKey = "apqmYfADARe5bX63TdOjhjqObIAY2nIwmfxCOekn";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, applicationID, clientKey);
    }
}
