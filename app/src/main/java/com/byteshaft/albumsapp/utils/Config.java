package com.byteshaft.albumsapp.utils;

import android.content.SharedPreferences;

public class Config {

    public static void setIsLoggedIn(boolean loggedIn) {
        SharedPreferences preferences = AppGlobals.getPreferences();
        preferences.edit().putBoolean(Constants.CONFIG_KEY_FIRST_RUN, loggedIn).apply();
    }

    public static boolean isLoggedIn() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getBoolean(Constants.CONFIG_KEY_FIRST_RUN, false);
    }
}
