package com.ruoqian.lib.activity;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

/**
 * 基Application
 */

public class BaseApplication extends Application{

    public static String AppPath;

    public static int width;
    public static int height;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        AppPath = getFilesDir().getPath();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
