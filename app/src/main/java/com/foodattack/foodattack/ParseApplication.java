package com.foodattack.foodattack;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;

/**
 * Created by Xue Hui on 5/7/2015.
 */
public class ParseApplication extends Application {

    private final String applicationID = "4sWMVHznnjN5mmMwwHe4tFiMbv2lpvPTpl3HGzil";
    private final String clientKey = "apqmYfADARe5bX63TdOjhjqObIAY2nIwmfxCOekn";

    @Override
    public void onCreate() {
        super.onCreate();

        //register subclasses
        ParseObject.registerSubclass(ShopListItem.class);
        ParseObject.registerSubclass(StockListItem.class);

        ParseCrashReporting.enable(this);


        //TODO remove enableLocalDatastore when rdy --> online access only
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, applicationID, clientKey);

        // TODO find out what these 3 lines do
//        ParseUser.enableAutomaticUser();
//        ParseACL defaultACL = new ParseACL();
//        ParseACL.setDefaultACL(defaultACL, true);
    }
}
