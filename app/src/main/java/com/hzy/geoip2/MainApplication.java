package com.hzy.geoip2;

import android.app.Application;

/**
 * Created by huzongrao on 16-9-22.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static MainApplication getApplication() {
        return mApplication;
    }

}
