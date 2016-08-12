package com.byteshaft.albumsapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

public class AppGlobals extends Application {

    private static Context sContext;
    private static SharedPreferences sPreferences;
    public static Typeface typeface;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/gobold_uplow.ttf");
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferences() {
        return sPreferences;
    }
}
