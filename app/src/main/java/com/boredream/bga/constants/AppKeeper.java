package com.boredream.bga.constants;

import android.content.Context;

public class AppKeeper {

    private AppKeeper() {

    }

    public static boolean isForeground = true;
    public static int screenWidth;

    private static Context app;

    public static void init(Context app) {
        AppKeeper.app = app;
    }

    public static Context getApp() {
        if(AppKeeper.app == null) {
            throw new RuntimeException("you must call AppKeeper.init(application) in your Application class first");
        }
        return app;
    }

}
